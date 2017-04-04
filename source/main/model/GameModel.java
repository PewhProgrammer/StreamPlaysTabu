package model;

import logic.GameControl;

import java.util.HashSet;
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


    public void GameModel(Language l, GameMode gm){
        mGameState = GameState.WaitingForPlayers;
        mNumPlayers = 0;
        registeredPlayers = new HashSet<String>();
        tabooWords = new HashSet<String>();
        lang = l;
        gameMode = gm;
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
}
