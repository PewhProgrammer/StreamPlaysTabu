package model;

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

    private Language lang;
    private GameMode gameMode;

    private LinkedList<String[]> qAndA;

    private Set<String> registeredPlayers;
    private Set<String> tabooWords;
    private Set<String> explanations;
    //TODO dynamic decay of guesses
    private String category, giver, word, winner;
    private LinkedList<Command> commands = new LinkedList<>();

    //best structure? idk ... contains vs. index tradeoff
    LinkedList<Guess> guesses = new LinkedList<>();

    private Bot bot;
    private SiteBot sbot;

    //könnte auch static sein denke ich
    private final short MIN_PLAYERS;


    public GameModel(Language l, short minPlayers){
        mGameState = GameState.WaitingForPlayers;
        mNumPlayers = 0;
        registeredPlayers = new HashSet<String>();
        tabooWords = new HashSet<String>();
        explanations = new HashSet<String>();
        qAndA = new LinkedList<>();
        lang = l;
        MIN_PLAYERS = minPlayers;
    }


    /******************* SETTER / GETTER ****************************/

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

    public String getCategory(String category) {
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
