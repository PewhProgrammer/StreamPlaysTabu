package logic;

import common.Log;
import logic.commands.CategoryChosen;
import logic.commands.Command;
import logic.commands.GiverJoined;
import model.GameModel;
import model.GameState;
import model.Observable;

import java.util.Date;
import java.util.Random;

import common.Util;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GameControl extends Observable{

    public static GameModel mModel;

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
                mModel.setGameState(GameState.GameStarted.Registration);
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

            //if user is registered but no giver, then new giver
            if(mModel.getRegisteredPlayers().size() > 0){

                if(mModel.getGiver().equals("")){
                    chooseNewGiver();
                    break;
                } //no previous giver
                else
                    chooseNewGiver();
            }

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


            if(mModel.getRegisteredPlayers().contains(
                    mModel.getWinner()
            )){
                // then send link
                mModel.setGiver(mModel.getWinner());
                break;
                //mModel.getBot().whisperLink("pewhTV","<Link>");
            }
            else mModel.setGiver(""); //s.t. there is no current giver and we have to choose new one
        }

        mModel.setGameState(GameState.WaitingForGiver);
        //TODO: delete next line!
        String[] votes = mModel.getPrevotedCategories();
        (new GiverJoined(mModel, "")).execute();
        Log.info("Starting the round");
        mModel.getBot().announceNewRound();
        //mModel.getCommands().push(new CategoryChosen(mModel,"","simulation"));
        new CategoryChosen(mModel,"",mModel.getPrevotedCategories()[0]).execute();
        mModel.getBot().whisperLink(mModel.getGiver(),mModel.getExplainWord());
        mModel.setTimeStamp();

        mModel.clearRegisteredPlayers();
        isStarted = true;
        runGame();
    }

    /**
     * handles new giver
     */
    private void chooseNewGiver(){
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
