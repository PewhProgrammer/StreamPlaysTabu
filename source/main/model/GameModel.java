package model;

import common.Neo4jWrapper;
import logic.bots.Bot;
import logic.bots.SiteBot;
import logic.commands.Command;

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
    LinkedList<Guess> guesses = new LinkedList<>();
    private LinkedList<String[]> qAndA;

    private String category, giver, word, winner;

    private Bot bot;
    private SiteBot sbot;

    public GameModel(Language l, short minPlayers, Neo4jWrapper neo, SiteBot siteBot){
        mGameState = GameState.WaitingForPlayers;
        mNumPlayers = 0;
        registeredPlayers = new HashSet<>();
        tabooWords = new HashSet<>();
        explanations = new HashSet<>();
        qAndA = new LinkedList<>();
        lang = l;
        MIN_PLAYERS = minPlayers;
        mOntologyDataBase = neo;
        sbot = siteBot;
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

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
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

    public void clearRegisteredPlayers() {
        registeredPlayers.clear();
    }

    public Set<String> getTabooWords() {
        return tabooWords;
    }

    public void generateTabooWords() {
        //TODO db query for giver lvl
        //TODO db query for taboo words
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
    }

    public void setCategory(String category) {
        this.category = category;
        notifyCategoryChosen();
    }

    public String getCategory() {
        return this.category;
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
        //TODO query db for explain word
        return word;
    }

    public void win(String winner) {
        this.winner  = winner;
        notifyWinner();
        //TODO update score of giver & winner & stream in db
        clear();
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public SiteBot getSiteBot() {
        return sbot;
    }

    public void clear() {
        clearExplanations();
        clearQAndA();
        setNumPlayers(0);
        clearRegisteredPlayers();
    }

}
