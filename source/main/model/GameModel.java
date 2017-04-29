package model;

import common.DatabaseException;
import common.Log;
import common.Neo4jWrapper;
import common.Util;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import logic.bots.Bot;
import logic.bots.SiteBot;
import logic.commands.Command;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.language.English;
import org.languagetool.language.German;
import org.languagetool.language.GermanyGerman;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.*;


public class GameModel extends Observable{

    private static final int LEVEL_2 = 100;
    private static final int LEVEL_3 = 300;
    private static final int LEVEL_4 = 700;
    private static final int LEVEL_5 = 1200;
    private static final int LEVEL_6 = 1800;

    private static final double ROUND_TIME = 1000.0 * 120.0;

    private GameState mGameState;
    private int mNumPlayers;
    private short MIN_PLAYERS;

    private Language lang;
    private GameMode gameMode;
    private final Neo4jWrapper mOntologyDataBase;

    private LinkedList<Command> commands = new LinkedList<>();

    private List<String> registeredPlayers;
    private Set<String> tabooWords;
    private Set<String> explanations;
    private Set<String> usedWords;
    private Set<String> votekick;
    private HashMap<String, TabooSuggestion> tabooSuggestions;
    private LinkedList<Guess> guesses;
    private LinkedList<String[]> qAndA;

    private String category, giver = "", word, winner;

    private ArrayList<PrevoteCategory> prevoting;

    private Date timeStamp;

    private Bot bot;
    private SiteBot sBot;

    private Set<String> hosts;

    private StanfordCoreNLP pipeline;
    private JLanguageTool langTool;

    public GameModel(Language l, short minPlayers, Neo4jWrapper neo, SiteBot siteBot){
        mGameState = GameState.Registration;
        mNumPlayers = 0;
        registeredPlayers = new ArrayList<>();
        tabooWords = new HashSet<>();
        explanations = new HashSet<>();
        qAndA = new LinkedList<>();
        lang = l;
        MIN_PLAYERS = minPlayers;
        mOntologyDataBase = neo;
        sBot = siteBot;
        hosts = new HashSet<>();
        prevoting = new ArrayList<>(10);
        usedWords = new HashSet<>();
        votekick = new HashSet<>();
        guesses = new LinkedList<>();
        tabooSuggestions = new HashMap<>();

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,natlog");
        pipeline = new StanfordCoreNLP(props);

        if (lang.equals(Language.Eng)) {
            langTool = new JLanguageTool(new BritishEnglish());
        } else {
            langTool = new JLanguageTool(new GermanyGerman());
        }
    }

    public GameState getGameState(){
        return mGameState;
    }

    public void setGameState(GameState mGameState) {
        this.mGameState = mGameState;
        notifyGameState();
    }

    public void setNumPlayers(int count){
        mNumPlayers = count;
    }

    public int getNumPlayers(){
        return mNumPlayers;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode() {
        this.gameMode = gameMode == GameMode.Normal ? GameMode.Streamer : GameMode.Normal;
        notifyGameMode();
    }

    public void setGameMode(GameMode gm) {
        this.gameMode = gm;
    }

    public Neo4jWrapper getNeo4jWrapper() {
        return mOntologyDataBase;
    }

    public LinkedList<Command> getCommands() {
        return commands;
    }

    public void setCommands(LinkedList<Command> commands) {
        this.commands = commands;
    }

    public Command pollNextCommand(){
        return commands.pollFirst();
    }

    public void pushCommand(Command e){
        commands.push(e);
    }

    public List<String> getRegisteredPlayers() {
        return registeredPlayers;
    }

    public void register(String user) {
        registeredPlayers.add(user);
        mNumPlayers++;
    }

    public void clearRegisteredPlayers() {
        registeredPlayers.clear();
    }

    public Set<String> getTabooWords() {
        return tabooWords;
    }

    public Set<String> generateTabooWords() {
        int score = mOntologyDataBase.getUserPoints(getGiver());
        int lvl = 1;

        if (score >= LEVEL_2 && score < LEVEL_3) {
            lvl = 2;
        }

        if (score >= LEVEL_3 && score < LEVEL_4) {
            lvl = 3;
        }

        if (score >= LEVEL_4 && score < LEVEL_5) {
            lvl = 4;
        }

        if (score >= LEVEL_5 && score < LEVEL_6) {
            lvl = 5;
        }

        if (score >= LEVEL_6) {
            lvl = 6;
        }

        //TODO db query for taboo words

        return tabooWords;
    }

    public Set<String> getExplanations() {
        return explanations;
    }

    public void addExplanation(String explanation) {
        explanations.add(explanation);

        notifyExplanation();
        String[] content = Util.parseTemplate(explanation);
        String relation = content[0].toLowerCase();
        String targetNode = content[1].toLowerCase();

        mOntologyDataBase.insertNodesAndRelationshipIntoOntology(word, targetNode, relation,true);
    }

    public void clearExplanations() {
        explanations.clear();
        notifyExplanation();
    }

    public void guess(String guess) {

        boolean contained = false;

        Iterator<Guess> it = guesses.iterator();
        while (it.hasNext()) {
            Guess g = it.next();
            g.decreaseScore();
            if (g.getGuess().equals(guess)) {
                g.occured();
                g.increaseScore();
                contained = true;
            }
        }

        if (!contained) {
            guesses.add(new Guess(guess));
        }

        Collections.sort(guesses);
        notifyGuess();
    }

    public LinkedList<Guess> getGuesses() {
        return guesses;
    }

    public void clearGuesses() {
        guesses.clear();
        notifyGuess();
    }

    public void addQAndA(String question, String answer) {
        qAndA.push(new String[]{question, answer});
        notifyQandA();

        if (!(answer.equals("yes") || answer.equals("no"))) {
            String[] content = Util.parseTemplate(answer);
            String relation = content[0].toLowerCase();
            String targetNode = content[1].toLowerCase();

            mOntologyDataBase.insertNodesAndRelationshipIntoOntology(word, targetNode, relation,false);
        }
    }

    public LinkedList<String[]> getQAndA() {
        return qAndA;
    }

    public void clearQAndA() {
        qAndA.clear();
        notifyQandA();
    }

    public void setCategory(String category) {
        this.category = category;
        notifyCategoryChosen();
    }

    public String getCategory() {
        return this.category;
    }

    public void prevote(int ID) {
        prevoting.get(ID).increaseScore();
    }

    public String[] getPrevotedCategories() {
        String[] prevotedCategories = new String[5];
        Collections.sort(prevoting);
        for (int i = 0; i < 5; i++) {
            prevotedCategories[i] = prevoting.get(i).getCategory();
        }
        return prevotedCategories;
    }

    public void generateVotingCategories() {
        for (int i = 0; i < 10; i++) {
            prevoting.add(i, new PrevoteCategory("Category " + Integer.toString(i + 1)));
        }
        //TODO get voting categories from db, create corresponding PrevoteCategory objects and fill arraylist
    }

    public String getGiver() {
        return giver;
    }

    public void setGiver(String giver) {
        this.giver = giver;
    }

    public String getExplainWord() {
        return word;
    }

    public String generateExplainWord() {
        //TODO query db for explain word that is not contained in usedWords
        try {
            word = mOntologyDataBase.getExplainWord(category,usedWords);
        }catch(DatabaseException e){
            e.getMessage();

        }
        usedWords.add(word);
        return word;
    }

    public void win(String winner) {
        this.winner  = winner;
        Date joinedTime = new Date();
        Date referenceTime = getTimeStamp();
        long diff = joinedTime.getTime() - referenceTime.getTime();
        double q = (double) diff / ROUND_TIME;
        int score = (int) ((tabooWords.size() + 1) * 100 - q * (tabooWords.size() + 1) * 100);

        Log.trace("Q: " + q + " and "+getTimeStamp() + " joined: " + joinedTime);
        Log.trace(winner + " gained Points: " + score);

        updateScore(winner, score);
        updateScore(giver, score);
        notifyWinner();
        getSiteBot().finish();
        getBot().announceWinner(winner);
        for (int i = 0; i < 3 && i < guesses.size(); i++) {
            if(guesses.get(i).getScore() > 1)
                mOntologyDataBase.insertNodesAndRelationshipIntoOntology(word, guesses.get(i).getGuess(), "is related to",true);
        }
        clear();
        setGiver(winner);
    }

    public String getWinner() {
        return this.winner;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public SiteBot getSiteBot() {
        return sBot;
    }

    public void clear() {
        clearExplanations();
        clearQAndA();
        clearGuesses();

        usedWords.clear();
        setNumPlayers(0);
        clearRegisteredPlayers();
    }

    public Set<String> getVotekick() {
        return votekick;
    }

    public void clearVotekick() {
        votekick.clear();
    }

    public void host(String host) {
        if (hosts.contains(host)) {
            bot.disconnectFromChatroom(host);
            hosts.remove(host);
        } else {
            bot.connectToChatroom(host);
            hosts.add(host);
        }
    }

    public int getScore(String user) {
        return mOntologyDataBase.getUserPoints(user);
    }

    public void updateScore(String user, int value) {
        int score = getScore(user) + value;
        score = Integer.max(0, score);
        mOntologyDataBase.updateUserPoints(user, score);
        notifyScoreUpdate();
    }

    public void tabooSuggetion(String suggestion) {
        if (!tabooSuggestions.containsKey(suggestion)) {
            TabooSuggestion ts = new TabooSuggestion(suggestion, word);
            tabooSuggestions.put(suggestion, ts);
        } else {
            tabooSuggestions.get(suggestion).occurred();
        }
    }

    public void transferTabooSuggestions() {

        ArrayList<TabooSuggestion> sortedSuggestions = new ArrayList<>(tabooSuggestions.size());
        for (TabooSuggestion ts : tabooSuggestions.values()) {
            sortedSuggestions.add(ts);
        }
        Collections.sort(sortedSuggestions);
        for (int i = 0; i < sortedSuggestions.size(); i++) {
            mOntologyDataBase.insertNodesAndRelationshipIntoOntology(sortedSuggestions.get(i).getExplanation(),
                    sortedSuggestions.get(i).getContent(), "isRelatedTo",true);
        }

        tabooSuggestions.clear();
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp() {
        timeStamp = new Date();
    }

    public List<String> lemmatize(String text) {
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<String> lemmas = new LinkedList<>();
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }

        return lemmas;
    }

    public void checkSpelling(String text) {
        try {
            List<RuleMatch> matches = langTool.check(text);
            for (RuleMatch match : matches) {
                System.out.println("Potential error at characters " +
                        match.getFromPos() + "-" + match.getToPos() + ": " +
                        match.getMessage());
                System.out.println("Suggested correction(s): " +
                        match.getSuggestedReplacements());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
