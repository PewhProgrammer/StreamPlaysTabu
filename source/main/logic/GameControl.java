package logic;

import common.Log;
import logic.commands.CategoryChosen;
import logic.commands.Command;
import logic.commands.GiverJoined;
import model.GameModel;
import model.GameState;
import model.Observable;

import java.util.Date;
import java.util.List;
import java.util.Random;

import common.Util;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameControl extends Observable{

    public static GameModel mModel;

    private boolean isStarted;
    private Random rand ;
    private final String extBindAddr;

    public GameControl(GameModel model,int seed,String ext_bindaddr){
        mModel = model;
        isStarted = false;
        mModel.setGameState(GameState.Config);
        rand = new Random(seed);
        extBindAddr = ext_bindaddr ;
    }

    /**
     * Game loop
     * @param
     * @return NULL
     */
    private void runGame(){

        //isStarted = (mModel.getGameState() == GameState.GameStarted);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date d = mModel.getTimeStamp();

        while(mModel.getGameState() == GameState.GameStarted){
            //processNextCommand();
            if(Util.diffTimeStamp(d,new Date()) > 120){
                mModel.setGiver("");
                mModel.getBot().announceNoWinner();
                mModel.setGameState(GameState.Lose);
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(mModel.getGameState() == GameState.Kick) mModel.setGiver("");
        isStarted = false;
        waitingForPlayers();

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
                Log.info("Starting new thread for commands processing...");
                processNextCommand();
                Log.info("Canceled Command processing");
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
    private void waitingForPlayers(){
        Log.info("Control is waiting for Players");
        while(mModel.getGameState() == GameState.Registration){

            mModel.setTimeStamp();
            try {
                //change this to 30 sec.
                mModel.getBot().announceRegistration();
                Log.info("30 seconds are running...");
                mModel.notifyRegistrationTime();
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if(mModel.getRegisteredPlayers().contains(mModel.getWinner())){
                mModel.setGiver(mModel.getWinner());
                break;
            }

            //if user is registered but no giver, then new giver
            if(mModel.getRegisteredPlayers().size() > 0){
                chooseNewGiver(mModel.getRegisteredPlayers());
                break;
            }

            //random giver
            chooseNewGiver(mModel.getBot().getUsers(mModel.getGiverChannel()));
            break;
        }

        mModel.setGameState(GameState.WaitingForGiver);
        Log.info("Starting the round");
        mModel.getBot().announceNewRound();
        mModel.getBot().whisperLink(mModel.getGiver(),extBindAddr, mModel.getSiteController().generatePW()); // send link
        mModel.setTimeStamp();

        while(mModel.getGameState() == GameState.WaitingForGiver){
            Date d = mModel.getTimeStamp();
            if(Util.diffTimeStamp(d,new Date()) > 20){
                mModel.getBot().announceGiverNotAccepted(mModel.getGiver());
                mModel.setGiver("");
                mModel.setGameState(GameState.GameStarted.Registration);
                break;
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mModel.setTimeStamp();

        mModel.clearRegisteredPlayers();
        isStarted = true;
        runGame();
    }

    /**
     * handles new giver
     */
    private void chooseNewGiver(List<String> users){
        Log.trace("New giver has been chosen from registration pool");
        int index = rand.nextInt(users.size());
        String newGiver =  users.get(index);
        mModel.setGiver(newGiver);
    }

    /**
     * sorgt dafür, dass die command queue abgearbeitet wird.
     */
    private void processNextCommand(){
        for (; ; ) {
            Command c = mModel.pollNextCommand();
            try {
                if(c.validate())
                    c.execute();
            }catch(NullPointerException n){
                try {

                    //Log.trace("No commands to be processed. sleeping...");
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
