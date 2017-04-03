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


    public void GameModel(Language l, GameMode gm){
        mGameState = GameState.WaitingForPlayers;
        mNumPlayers = 0;
        registeredPlayers = new HashSet<String>();
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
