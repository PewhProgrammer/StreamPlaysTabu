package model;

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

    private Language lang;
    private GameMode gameMode;

    private Set<String> registeredPlayers;
    private Set<String> tabooWords;
    //TODO List for guesses + num of occurences
    private String category, giver, word, explanation, question, answer;
    private LinkedList<Command> commands = new LinkedList<>();

    private Bot bot;
    private SiteBot sbot;

    private short MIN_PLAYERS;


    public GameModel(Language l, short minPlayers){
        mGameState = GameState.WaitingForPlayers;
        mNumPlayers = 0;
        registeredPlayers = new HashSet<String>();
        tabooWords = new HashSet<String>();
        lang = l;
        MIN_PLAYERS = minPlayers;
    }


    /******************* SETTER / GETTER ****************************/


    public GameState getGameState(){
        return mGameState;
    }

    public void setGameState(GameState mGameState) {
        this.mGameState = mGameState;
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
}
