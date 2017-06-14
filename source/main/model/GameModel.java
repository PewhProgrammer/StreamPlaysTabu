package model;

import common.database.DatabaseException;
import common.Log;
import common.database.Neo4jWrapper;
import common.Util;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import gui.webinterface.SiteController;
import logic.bots.AltTwitchBot;
import logic.bots.BeamBot;
import logic.bots.Bot;
import logic.commands.Command;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;
//import org.languagetool.JLanguageTool;
//import org.languagetool.language.BritishEnglish;
//import org.languagetool.language.GermanyGerman;
//import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class GameModel extends Observable{

    private static final int LEVEL_2 = 100;
    private static final int LEVEL_3 = 300;
    private static final int LEVEL_4 = 700;
    private static final int LEVEL_5 = 1200;
    private static final int LEVEL_6 = 1800;

    private static final double THIRTY_MINUTES = 1000.0 * 60.0 * 30.0;

    private GameState mGameState;
    private int mNumPlayers;
    private double roundTime = 105;
    private int errCounter = 0;

    //private Language lang;
    private GameMode gameMode;
    private final Neo4jWrapper mOntologyDataBase;

    private LinkedList<Command> commands = new LinkedList<>();

    private List<String> registeredPlayers;
    private List<String> explanations;
    private Set<String> tabooWords;
    private Set<String> usedWords;
    private Set<String> votekick;
    private HashMap<String, TabooSuggestion> tabooSuggestions;
    private LinkedList<Guess> guesses;
    private LinkedList<String[]> qAndA;

    private HashMap<String, Integer> votingUser;

    private String validationKey;
    private Set<String> validationObjects;

    private String giverChannel = "";
    private String gameOutcome = "Not determined";

    private String category, giver = "", word, winner;
    private int gainedPoints = 0;
    private int difficultyLevel = 1 ;
    private int missedOffer = 0 ;

    private int validationLevel = -1 ;

    private ArrayList<PrevoteCategory> prevoting;
    public static ReentrantLock prevotingLock = new ReentrantLock();

    private Date timeStamp;

    private Bot bot;
    private SiteController sCon;

    private Set<String> hosts;
    private HashMap<String, AltTwitchBot> hostBots;

    private StanfordCoreNLP pipeline;

    public GameModel(Neo4jWrapper neo){
        mGameState = GameState.Registration;
        mNumPlayers = 0;
        registeredPlayers = new ArrayList<>();
        tabooWords = new HashSet<>();
        explanations = new LinkedList<>();
        mOntologyDataBase = neo;
        hosts = new HashSet<>();
        hostBots = new HashMap<>();
        prevoting = new ArrayList<>(10);
        usedWords = new HashSet<>();
        votekick = new HashSet<>();
        guesses = new LinkedList<>();
        tabooSuggestions = new HashMap<>();
        votingUser = new HashMap<>();

        qAndA = new LinkedList<>();

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,natlog");
        pipeline = new StanfordCoreNLP(props);

        generateVotingCategories();
    }

    public Set<String> getHosts() {
        return this.hosts;
    }

    public void setBot(String platform, String channel) {
        try {
            this.bot = platform.equals("Twitch") ? new AltTwitchBot(this, "#" + channel) : new BeamBot(this, channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameState getGameState(){
        return mGameState;
    }

    public void setGameState(GameState mGameState) {
        System.out.println(" GameState set to " +  mGameState);
        this.mGameState = mGameState;
        notifyGameState();
    }

    public void setNumPlayers(int count){
        mNumPlayers = count;
    }

    public int getNumPlayers(){
        return mNumPlayers;
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
        notifyGameMode();
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

    public Set<String> generateTabooWords(String ch) {
        int score = mOntologyDataBase.getUserPoints(getGiver(),ch);
        int lvl = getLevel(score);
        tabooWords.clear();
        tabooWords.addAll(mOntologyDataBase.getTabooWords(getExplainWord(),this.category,lvl-1));

        notifyTabooWords();

        return tabooWords;
    }

    public int getLevel(int score) {
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
        this.difficultyLevel = lvl;
        return lvl;
    }

    public List<String> getExplanations() {
        return explanations;
    }

    public void addExplanation(String explanation) {
        explanations.add(explanation);

        notifyExplanation();
        String[] content = Util.parseTemplate(explanation);
        String relation = content[0].toLowerCase();
        String targetNode = content[1].toLowerCase();
        String att = content[2].toLowerCase();
        boolean isExplain = Boolean.parseBoolean(content[3]);

        mOntologyDataBase.insertNodesAndRelationshipIntoOntology(word, targetNode, isExplain, relation,true, att);
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

        if (!(answer.equals("Yes") || answer.equals("No"))) {

            String[] content = Util.parseTemplate(answer);
            String relation = content[0].toLowerCase();
            String targetNode = content[1].toLowerCase();
            String att = content[2].toLowerCase();
            boolean isExplain = false;

            mOntologyDataBase.insertNodesAndRelationshipIntoOntology(word, targetNode, isExplain, relation,false, att);
        } else {
            mOntologyDataBase.createQuestion(word,question,answer);
        }
    }

    public LinkedList<String[]> getQAndA() {
        return qAndA;
    }

    public void clearQAndA() {
        qAndA.clear();
        notifyQandA();
    }

    public int getValidationLevel() {
        return validationLevel;
    }

    public void setValidationLevel(int validationLevel) {
        this.validationLevel = validationLevel;
    }

    public void setCategory(String category) {
        this.category = category;
        notifyCategoryChosen();
    }

    public void incMissedOffer(){missedOffer += 1;}

    public String getCategory() {
        return this.category;
    }

    public void prevote(int ID, String s) {
        prevotingLock.lock();
        if (!votingUser.containsKey(s)) {
            votingUser.put(s, 0);
        }
        if (votingUser.get(s) < 5) {
            if (prevoting.get(ID).increaseScore(s)) {
                int i = votingUser.get(s);
                votingUser.remove(s);
                votingUser.put(s, ++i);
            }
        }
        prevotingLock.unlock();
    }

    public ArrayList<PrevoteCategory> getPrevotedCategories() {
        prevotingLock.lock();
        Collections.sort(prevoting);
        prevotingLock.unlock();
        return prevoting;
    }

    public String[] getPrevoteCategories() {
        String[] prevotedCategories = new String[10];
        prevotingLock.lock();
        Iterator<PrevoteCategory> it = prevoting.iterator();

        for (int i = 0; i < 10; i++) {
            if (it.hasNext()) {
                prevotedCategories[i] = it.next().getCategory();
            } else {
                break;
            }
        }
        prevotingLock.unlock();

        return prevotedCategories;
    }

    public void generateVotingCategories() {
        if (mOntologyDataBase != null) {
            Log.info("Generating Prevote Categories");
            Set<String> categories = mOntologyDataBase.getCategories(10);
            Iterator<String> it = categories.iterator();
            for (int i = 0; i < 10; i++) {
                if (!it.hasNext()) {
                    break;
                }
                String category = it.next();
                PrevoteCategory pc = new PrevoteCategory(category);
                prevotingLock.lock();
                if (prevoting.contains(pc)) {
                    continue;
                }
                prevoting.add(i, pc);
                prevotingLock.unlock();
            }
        }
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
        try {
            word = mOntologyDataBase.getExplainWord(category,usedWords);
            notifyExplainWord();
        }catch(DatabaseException e){
            e.getMessage();

        }
        usedWords.add(word);
        return word;
    }

    public String getValidationKey() {
        return validationKey;
    }

    public void setValidationKey(String validationKey) {
        this.validationKey = validationKey;
    }

    public Set<String> getValidationObjects() {
        return validationObjects;
    }

    public void setValidationObjects(Set<String> validationObjects) {
        this.validationObjects = validationObjects;
    }

    public void setExplainWord(String word) {
        this.word = word;
    }

    public void setTabooWords(Set<String> taboos) {
        this.tabooWords = taboos;
    }

    public void win(String winner,String ch) {
        this.gameOutcome = GameState.Win.toString();

        if (getGameState() != GameState.GameStarted || !getGameState().equals(GameState.GameStarted)) {
            System.out.println("!= : " + (getGameState() != GameState.GameStarted) + " ; " + "equals : " + (!getGameState().equals(GameState.GameStarted)));
            return;
        }

        this.winner  = winner;
        Date joinedTime = new Date();
        Date referenceTime = getTimeStamp();
        long diff = joinedTime.getTime() - referenceTime.getTime();
        double t = roundTime * 1000.0;
        double q = (double) diff / t;
        int score = (int) ((tabooWords.size() + 1) * 100 - q * (tabooWords.size() + 1) * 100);

        Log.trace("Q: " + q + " and "+getTimeStamp() + " joined: " + joinedTime);
        Log.trace(winner + " gained Points: " + score);

        gainedPoints = score;

        updateScore(winner, score,ch);
        updateScore(giver, score,ch);
        setGameState(GameState.Win);
        notifyWinner();
        announceWinner(winner);
        for (int i = 0; i < 3 && i < guesses.size(); i++) {
            if(guesses.get(i).getScore() > 1)
                mOntologyDataBase.insertNodesAndRelationshipIntoOntology(guesses.get(i).getGuess(),word,true, "is related to",true,"");
        }
        clear();
        setGiver(winner);
    }

    public int getGainedPoints() {
        return gainedPoints;
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

    public void setSiteController(SiteController sc) {
        this.sCon = sc;
    }

    public SiteController getSiteController() {
        return sCon;
    }

    public void clear() {

        Date now = new Date() ;
        int playRoundTime = getRoundTime() - (int)Util.diffTimeStamp(timeStamp,now)  ;
        playRoundTime = playRoundTime >= 0 ? playRoundTime : 0 ;

        //update game instance
        try {
            mOntologyDataBase.updateNewGame(playRoundTime, giver, difficultyLevel, missedOffer,
                    guesses, qAndA, registeredPlayers, tabooWords, usedWords, explanations,
                    word, gameOutcome, gameMode
            );
        }catch(ServiceUnavailableException e){
            Log.error(e.getLocalizedMessage());
        }

        missedOffer = 0 ;
        setGameOutcome("Not determined");
        setRoundTime(105);
        clearExplanations();
        clearQAndA();
        clearGuesses();
        clearVotekick();
        errCounter = 0;
        prevotingLock.lock();
        prevoting.clear();
        prevotingLock.unlock();
        usedWords.clear();
        setNumPlayers(0);
        setValidationLevel(-1);
        votingUser.clear();
        clearRegisteredPlayers();
        generateVotingCategories();
    }

    public void setGameOutcome(String s){this.gameOutcome = s;}

    public void setRoundTime(int i ){
        this.roundTime = i;
    }

    public int getRoundTime(){
        return (int) this.roundTime;
    }

    public int increaseErrCounter() {
        return ++errCounter;
    }

    public Set<String> getVotekick() {
        return votekick;
    }

    public void clearVotekick() {
        votekick.clear();
    }

    public void host(String host, AltTwitchBot hostBot) {
        hosts.add(host);
        hostBots.put(host, hostBot);
        if (getGameMode() != GameMode.HOST) {
            setGameMode(GameMode.Normal);
        }
    }

    public void unhost(String host) {
        hosts.remove(host);
        hostBots.remove(host);
        if (hosts.isEmpty()) {
            setGameMode(GameMode.Normal);
        }
    }

    public String getGiverChannel() {
        return giverChannel;
    }

    public void setGiverChannel(String giverChannel) {
        this.giverChannel = giverChannel;
    }

    public int getScore(String user,String ch) {
        return mOntologyDataBase.getUserPoints(user,ch);
    }

    public void updateScore(String user, int value,String ch) {
        int score = getScore(user,ch) + value;
        score = Integer.max(0, score);
        mOntologyDataBase.updateUserPoints(user, score, ch);
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
            //TODO tim fragen wies funktioniert
            mOntologyDataBase.insertNodesAndRelationshipIntoOntology(sortedSuggestions.get(i).getExplanation(),
                    sortedSuggestions.get(i).getContent(),false, "isRelatedTo",true,"");
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

    public HashMap<String, AltTwitchBot> getHostBots() {
        return hostBots;
    }

    public boolean contribute(String user, String channel) {

        if (mOntologyDataBase.getUserError(user, channel) < 4) {
            return true;
        }

        //Jahr, Tag, Stunde, Minute
        String[] stamp = mOntologyDataBase.getUserErrorTimeStamp(user);
        DateFormat df = new SimpleDateFormat("yyyy-dd-HH-mm-ss");
        Date date = null;
        try {
            date = df.parse(stamp[0] + "-" + stamp[1] + "-" + stamp[2] + "-" + stamp[3] + "-00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            double d = Util.diffTimeStamp(date, new Date());
            if (d > THIRTY_MINUTES) {
                return true;
            }
        }

        return false;
    }

    public void announceNewRound() {
        for (AltTwitchBot ab : hostBots.values()) {
            ab.announceNewRound();
        }
        bot.announceNewRound();
    }

    public void announceWinner(String winner) {
        for (AltTwitchBot ab : hostBots.values()) {
            ab.announceWinner(winner);
        }
        bot.announceWinner(winner);
    }

    public void announceNoWinner() {
        for (AltTwitchBot ab : hostBots.values()) {
            ab.announceNoWinner();
        }
        bot.announceNoWinner();
    }

    public void announceGiverNotAccepted(String user) {
        for (AltTwitchBot ab : hostBots.values()) {
            ab.announceGiverNotAccepted(user);
        }
        bot.announceGiverNotAccepted(user);
    }

    public void announceRegistration() {
        for (AltTwitchBot ab : hostBots.values()) {
            ab.announceRegistration();
        }
        bot.announceRegistration();
    }

    public void announceScore(String channel, String user, int score) {
        if (channel.equals(giverChannel)) {
            bot.announceScore(user, score);
        } else {
            hostBots.get(channel).announceScore(user, score);
        }
    }

    public void whisperRules(String channel, String user) {
        if (channel.equals(giverChannel)) {
            bot.whisperRules(user);
        } else {
            AltTwitchBot ab = hostBots.get(channel);
            if (ab == null) {
                throw new IllegalArgumentException("Not a valid channel");
            }
            ab.whisperRules(user);
        }
    }
}
