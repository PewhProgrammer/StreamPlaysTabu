package logic;

import common.Log;
import logic.commands.CategoryChosen;
import logic.commands.Command;
import logic.commands.GiverJoined;
import model.GameMode;
import model.GameModel;
import model.GameState;
import model.Observable;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
            if(Util.diffTimeStamp(d,new Date()) > 105){
                mModel.setGiver("");
                mModel.getBot().announceNoWinner();
                mModel.setGameState(GameState.Lose);
                mModel.clear();
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(mModel.getGameState() == GameState.Kick) mModel.setGiver("");
        try {
            Thread.sleep(5000);
            mModel.setGameState(GameState.GameStarted.Registration);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        gameLoop:
        while(mModel.getGameState() == GameState.Registration || !isStarted){

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

            if(mModel.getGameMode() == GameMode.Streamer){
                mModel.setGiver(mModel.getGiverChannel());
                break gameLoop;
            }

            if(mModel.getRegisteredPlayers().contains(mModel.getWinner())){
                mModel.setGiver(mModel.getWinner());
                if(!mModel.getGiver().equals(""))
                    break gameLoop;
            }

            while(true){
                try {
                    //if user is registered but no giver, then new giver
                    if(mModel.getRegisteredPlayers().size() > 0){
                        chooseNewGiver(mModel.getRegisteredPlayers());
                        if(!mModel.getGiver().equals("")){
                            break gameLoop;
                        }
                    }
                    Log.trace("Entering Stand by: Anyone can type !register to become giver");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            /*
            //random giver
            List<String> usersInChannel = mModel.getBot().getUsers(mModel.getGiverChannel()) ;
            if(usersInChannel.size() > 1) {
                chooseNewGiver(mModel.getBot().getUsers(mModel.getGiverChannel()));
                if(!mModel.getGiver().equals(""))
                    break;
            }*/


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
                mModel.clear();
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
        users = users.stream().filter(
               user -> !user.equals("streamplaystaboo")).collect(Collectors.toList());
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
                if(c.validate()) {
                    Log.trace(c.toString()+ " Command received!");
                    c.execute();
                }
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
