package model;

import logic.GameControl;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameModel extends Observable{

    private GameState mGameState;
    private int mNumPlayers;


    public void GameModel(){
        mGameState = GameState.WaitingForPlayers;
        mNumPlayers = 0 ;
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
