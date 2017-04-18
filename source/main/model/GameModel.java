package model;

import common.Neo4jWrapper;
import logic.bots.Bot;
import logic.bots.SiteBot;
import logic.commands.Command;
import logic.commands.Prevote;

import java.util.*;


public class GameModel extends Observable{

    private GameState mGameState;
    private int mNumPlayers;
    private short MIN_PLAYERS;

    private Language lang;
    private GameMode gameMode;
    private final Neo4jWrapper mOntologyDataBase;

    private LinkedList<Command> commands = new LinkedList<>();

    private Set<String> registeredPlayers;
    private Set<String> tabooWords;
    private Set<String> explanations;
    private Set<String> usedWords;
    private Set<String> votekick;
    LinkedList<Guess> guesses = new LinkedList<>();
    private LinkedList<String[]> qAndA;

    private String category, giver, word, winner;

    private ArrayList<PrevoteCategory> prevoting;

    private Bot bot;
    private SiteBot sBot;

    private Set<String> hosts;

    public GameModel(Language l, short minPlayers, Neo4jWrapper neo, SiteBot siteBot){
        mGameState = GameState.Registration;
        mNumPlayers = 0;
        registeredPlayers = new HashSet<>();
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

    public short getMIN_PLAYERS() {
        return MIN_PLAYERS;
    }

    public void setMIN_PLAYERS(short MIN_PLAYERS) {
        this.MIN_PLAYERS = MIN_PLAYERS;
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

    public Set<String> getRegisteredPlayers() {
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
        //TODO db query for giver lvl
        //TODO db query for taboo words

        return tabooWords;
    }

    public Set<String> getExplanations() {
        return explanations;
    }

    public void addExplanation(String explanation) {
        explanations.add(explanation);
        //TODO parse template
        //TODO update database
        notifyExplanation();
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
            if (g.getGuess().equals(guess)) {
                g.occured();
                g.increaseScore();
                Collections.sort(guesses);
                contained = true;
                break;
            }
        }

        if (!contained) {
            guesses.add(new Guess(guess));
        }

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

        //TODO parse template
        //TODO update database
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
        //TODO get voting categories from db, create corresponding PrevoteCategory objects and fill arraylist
    }

    public String getGiver() {
        return giver;
    }

    public void setGiver(String giver) {
        this.giver = giver;
    }

    public String getExplainWord() {
        String exp = "";
        //TODO db query for word
        word = exp;
        return word;
    }

    public String generateExplainWord() {
        //TODO query db for explain word that is not contained in usedWords
        return word;
    }

    public void win(String winner) {
        this.winner  = winner;
        //TODO score
        int score = 10;
        updateScore(winner, score);
        notifyWinner();
        getSiteBot().finish();
        getBot().announceWinner();
        //TODO put top guesses in db
        clear();
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
        //TODO db query for user score
        return 0;
    }

    public void updateScore(String user, int value) {
        int score = getScore(user) + value;
        score = Integer.max(0, score);
        //TODO write user's score to db
        notifyScoreUpdate();
    }
}
