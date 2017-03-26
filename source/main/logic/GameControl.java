package logic;

import common.Log;
import model.GameModel;
import model.GameState;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameControl {

    private final short MIN_PLAYERS = 2 ;

    private GameModel mModel;
    private boolean isStarted;

    public void GameControl(){
        mModel = new GameModel();
        isStarted = false;
    }


    /**
     * brauchen wir als modul, da startGame() es wieder aufrufen muss
     * @param
     * @return NULL
     */
    public void waitingForPlayers(){
        Log.trace("Control is waiting for Players");
        while(mModel.getGameState() == GameState.WaitingForPlayers){
            //process twitch/beam api
            if(mModel.getNumPlayers() >= MIN_PLAYERS)
                mModel.setGameState(GameState.GameStarted);
        }
        Log.trace("Players available to play the game");
        startGame();
    }

    /**
     * Game loop
     * @param
     * @return NULL
     */
    public void startGame(){

        Log.trace("Control started the game");
        isStarted = (mModel.getGameState() == GameState.GameStarted);
        while(isStarted){
            //I suggest state pattern for actual gameplay and splitscreen/highscore

            //
        }
        Log.trace("Control ends the game");

    }

}
