package logic;

import common.Log;
import common.Neo4jWrapper;
import javafx.application.Platform;
import logic.bots.Bot;
import logic.bots.SiteBot;
import logic.commands.Command;
import logic.commands.Register;
import model.GameModel;
import model.GameState;
import model.Language;
import model.Observable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameControl extends Observable{

    private GameModel mModel;

    private boolean isStarted;
    private Random rand ;

    public GameControl(GameModel model,int seed){
        mModel = model;
        isStarted = false;
        mModel.setGameState(GameState.Config);
        rand = new Random(seed);
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
            //processNextCommand();
            if(mModel.getGameState() == GameState.Registration) {
               //mModel.notifyGameState();
               waitingForPlayers();
            }
        }
        Log.trace("Control ends the game");

    }

    public void waitingForConfig(){
        while(mModel.getGameState() != GameState.Registration){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread mTHREAD = new Thread() {
            @Override
            public void run() {
                Log.info("processing Commands...");
                processNextCommand();
            }

        } ;

        mTHREAD.start();
        waitingForPlayers();
    }

    /**
     * brauchen wir als modul, da runGame() es wieder aufrufen muss
     * @param
     * @return NULL
     */
    public void waitingForPlayers(){
        Log.info("Control is waiting for Players");
        while(mModel.getGameState() == GameState.Registration){
            //if user is registered but no giver, then new giver
            if(mModel.getRegisteredPlayers().size() > 0){
                chooseNewGiver();
            }
            //mModel.notifyRegistrationTime();

            mModel.setTimeStamp();
            try {
                //change this to 30 sec.
                Log.info("10 seconds are running...");
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(mModel.getRegisteredPlayers().contains(
                    mModel.getGiver()
            )){
                // then send link
            }

        }

        Log.info("Starting the round");
        mModel.notifyGameState();

        mModel.clearRegisteredPlayers();
        isStarted = true;
        runGame();
    }

    /**
     * handles new giver
     */
    public void chooseNewGiver(){
        Log.info("New giver has been chosen");
        int index = rand.nextInt(mModel.getRegisteredPlayers().size());
        String newGiver =  mModel.getRegisteredPlayers().get(index);
        mModel.setGiver(newGiver);
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
            }catch(NullPointerException n){
                try {

                    Log.trace("No commands to be processed. sleeping...");
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch(Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
