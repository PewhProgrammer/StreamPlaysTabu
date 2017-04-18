package model;

import common.Neo4jWrapper;
import logic.GameControl;
import logic.bots.Bot;
import logic.bots.SiteBot;
import logic.commands.Command;
import sun.awt.image.ImageWatched;

import java.util.*;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameModel extends Observable{

    private GameState mGameState;
    private int mNumPlayers;
    private final short MIN_PLAYERS;

    private Language lang;
    private GameMode gameMode;
    private final Neo4jWrapper mOntologyDataBase;

    private LinkedList<String[]> qAndA;
    private LinkedList<Command> commands = new LinkedList<>();

    private Set<String> registeredPlayers;
    private Set<String> tabooWords;

    private String explainBear;

    public GameState getmGameState() {
        return mGameState;
    }

    public void setmGameState(GameState mGameState) {
        this.mGameState = mGameState;
    }

    public int getmNumPlayers() {
        return mNumPlayers;
    }

    public void setmNumPlayers(int mNumPlayers) {
        this.mNumPlayers = mNumPlayers;
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

    public Neo4jWrapper getmOntologyDataBase() {
        return mOntologyDataBase;
    }

    public LinkedList<String[]> getqAndA() {
        return qAndA;
    }

    public void setqAndA(LinkedList<String[]> qAndA) {
        this.qAndA = qAndA;
    }

    public LinkedList<Command> getCommands() {
        return commands;
    }

    public void setCommands(LinkedList<Command> commands) {
        this.commands = commands;
    }

    public Set<String> getRegisteredPlayers() {
        return registeredPlayers;
    }

    public void setRegisteredPlayers(Set<String> registeredPlayers) {
        this.registeredPlayers = registeredPlayers;
    }

    public Set<String> getTabooWords() {
        return tabooWords;
    }

    public void setTabooWords(Set<String> tabooWords) {
        this.tabooWords = tabooWords;
    }

    public Set<String> getExplanations() {
        return explanations;
    }

    public void setExplanations(Set<String> explanations) {
        this.explanations = explanations;
    }

    public String getGiver() {
        return giver;
    }

    public void setGiver(String giver) {
        this.giver = giver;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public SiteBot getSbot() {
        return sbot;
    }

    public void setSbot(SiteBot sbot) {
        this.sbot = sbot;
    }

    private Set<String> explanations;

    //TODO dynamic decay of guesses
    private String category, giver, word, winner;
    //best structure? idk ... contains vs. index tradeoff
    LinkedList<Guess> guesses = new LinkedList<>();

    private Bot bot;
    private SiteBot sbot;


    public GameModel(Language l, short minPlayers, Neo4jWrapper neo){
        mGameState = GameState.WaitingForPlayers;
        mNumPlayers = 0;
        registeredPlayers = new HashSet<String>();
        tabooWords = new HashSet<String>();
        explanations = new HashSet<String>();
        qAndA = new LinkedList<>();
        lang = l;
        MIN_PLAYERS = minPlayers;
        mOntologyDataBase = neo;
    }



    /******************* SETTER / GETTER ****************************/


    public Neo4jWrapper getNeo4jWrapper() {
        return mOntologyDataBase;
    }

    public SiteBot getSiteBot() {
        return sbot;
    }

    public Bot getStreamBot() {
        return bot;
    }

    public GameState getGameState(){
        return mGameState;
    }

    public void setGameState(GameState mGameState) {
        this.mGameState = mGameState;
        notifyGameState();
    }

    public void setCategory(String category) {
        this.category = category;
        notifyCategoryChosen();
    }

    public String getExplainWord() {
        String exp = "";
        //TODO db query for word
        word = exp;
        return word;
    }

    public Set<String> getTabooWords() {
        int lvl = 1;
        //TODO db query for giver lvl
        //TODO db query for taboowords

        return tabooWords;
    }

    public String getCategory() {
        return this.category;
    }

    public void setNumPlayers(int count){
        mNumPlayers = count;
    }

    public int getNumPlayers(){
        return mNumPlayers;
    }

    public Command pollNextCommand(){
        return commands.pollFirst();
    }

    public void pushCommand(Command e){
        commands.push(e);
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

    public void addExplanation(String explanation) {
        explanations.add(explanation);
        //TODO parse template
        //TODO update database
        notifyExplanation();
    }

    public void clearExplanations() {
        explanations.clear();
    }

    public void clearRegisteredPlayers() {
        registeredPlayers.clear();
    }

    public void clear() {
        clearExplanations();
        clearQAndA();
        setNumPlayers(0);
        clearRegisteredPlayers();
    }

    public void win(String winner) {
        this.winner  = winner;
        notifyWinner();
        //TODO update score of giver & winner & stream in db
        clear();
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

}
