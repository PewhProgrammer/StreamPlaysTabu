package logic;

import common.Log;
import logic.bots.Bot;
import logic.bots.SiteBot;
import model.GameModel;
import model.GameState;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameControl {

    private final short MIN_PLAYERS = 2 ;

    private GameModel mModel;
    private Bot bot;
    private SiteBot sbot;

    private boolean isStarted;

    public void GameControl(){
        mModel = new GameModel();
        isStarted = false;
    }


    /**
     * setup game before main game starts
     */
    public void setupGame() {

    }

    /**
     * Game loop
     * @param
     * @return NULL
     */
    public void runGame(){

        Log.trace("Control started the game");
        isStarted = (mModel.getGameState() == GameState.GameStarted);
        while(isStarted){
            //I suggest state pattern for actual gameplay and splitscreen/highscore

            //
        }
        Log.trace("Control ends the game");

    }

    /**
     * brauchen wir als modul, da runGame() es wieder aufrufen muss
     * @param
     * @return NULL
     */
    private void waitingForPlayers(){
        Log.trace("Control is waiting for Players");
        while(mModel.getGameState() == GameState.WaitingForPlayers){
            //process twitch/beam api
            if(mModel.getNumPlayers() >= MIN_PLAYERS)
                mModel.setGameState(GameState.GameStarted);
        }
        Log.trace("Players available to play the game");
        runGame();
    }

    /**
     * initialize registration phase
     */
    private void registration() {

    }
}
