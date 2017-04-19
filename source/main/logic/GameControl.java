package logic;

import common.Log;
import common.Neo4jWrapper;
import logic.bots.Bot;
import logic.bots.SiteBot;
import logic.commands.Command;
import logic.commands.Register;
import model.GameModel;
import model.GameState;
import model.Language;
import model.Observable;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameControl extends Observable{

    private GameModel mModel;

    private boolean isStarted;

    public GameControl(GameModel model){
        mModel = model;
        isStarted = false;
    }

    /**
     * Game loop
     * @param
     * @return NULL
     */
    private void runGame(){

        Log.trace("Control started the game");
        isStarted = (mModel.getGameState() == GameState.GameStarted);

        while(isStarted){
            processNextCommand();
            if(mModel.getGameState() == GameState.Registration)
                waitingForPlayers();
        }
        Log.trace("Control ends the game");

    }

    /**
     * brauchen wir als modul, da runGame() es wieder aufrufen muss
     * @param
     * @return NULL
     */
    public void waitingForPlayers(){
        Log.info("Control is waiting for Players");
        while(mModel.getGameState() == GameState.Registration){
            //if user is not registered
            if(mModel.getRegisteredPlayers().size() > 0
                    )
            mModel.notifyRegistrationTime();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                }
            }, 2*60*1000);

            processNextCommand();

        }
        Log.trace("Players are available to play the game");
        isStarted = true;
        runGame();
    }

    /**
     * sorgt dafür, dass die command queue abgearbeitet wird.
     */
    private void processNextCommand(){
        for (; ; ) {
            Command c = mModel.pollNextCommand();
            try {
                c.validate();
                c.execute();
                break;
            } catch(Exception e) {
                break;
            }
        }
    }
}
