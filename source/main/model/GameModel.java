package model;

import common.Neo4jWrapper;
import logic.GameControl;
import logic.bots.Bot;
import logic.bots.SiteBot;
import logic.commands.Command;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameModel extends Observable{

    private GameState mGameState;
    private int mNumPlayers;
    private short MIN_PLAYERS;

    private Language lang;
    private GameMode gameMode;
    private final Neo4jWrapper mOntologyDataBase;

    private LinkedList<String[]> qAndA;
    private LinkedList<Command> commands = new LinkedList<>();

    private Set<String> registeredPlayers;
    private Set<String> tabooWords;
    private Set<String> explanations;

    //TODO List for guesses + num of occurences
    private String category, giver, word;

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
        //TODO inform observer
    }

    public void setCategory(String category) {
        this.category = category;
        //TODO inform observer
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
        //TODO inform observer
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
        //TODO inform observer
        //TODO update database
    }

    public void clearExplanations(String explanation) {
        explanations.clear();
    }

}
