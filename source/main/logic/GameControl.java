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
import java.util.Set;
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
            if(Util.diffTimeStamp(d,new Date()) > mModel.getRoundTime()){
                mModel.announceNoWinner();
                mModel.setGameState(GameState.Lose);
                mModel.setGameOutcome("Lose");
                mModel.clear();
                mModel.setGiver("");
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(mModel.getGameState() == GameState.Kick) {
            mModel.setGiver("");
        }
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
            Set<String> hosts = mModel.getHosts();
            int p = 0;
            for (String host : hosts) {
                p += mModel.getBot().getUsers(host).size();
            }
            p += mModel.getBot().getUsers(mModel.getGiverChannel()).size();
            if(p < 2) mModel.notifyUpdateTimerText("Waiting for minimum of 2 players!");
            while(p < 2){
                sleepThread(10);
                p = 0;
                for (String host : hosts) {
                    p += mModel.getBot().getUsers(host).size();
                }
                p += mModel.getBot().getUsers(mModel.getGiverChannel()).size();
            }
            mModel.notifyUpdateTimerText("go marci boi");
            mModel.setTimeStamp();
            mModel.announceRegistration();
            sleepThread(30);

            if(mModel.getGameMode() == GameMode.Streamer){
                mModel.setGiver(mModel.getGiverChannel()); break gameLoop;
            }

            if(mModel.getRegisteredPlayers().contains(mModel.getWinner())){
                mModel.setGiver(mModel.getWinner());
                if(!mModel.getGiver().equals("")) break gameLoop;
            }

            Log.info("Entering Stand by: Anyone can type !register to become giver");
            mModel.notifyUpdateTimerText("Next player to register will be the next giver!");
            while(true){
                try {
                    //if user is registered but no giver, then new giver
                    if(mModel.getRegisteredPlayers().size() > 0){
                        chooseNewGiver(mModel.getRegisteredPlayers());
                        if(!mModel.getGiver().equals("")){
                            break gameLoop;
                        }

                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        mModel.setGameState(GameState.WaitingForGiver);
        Log.info("Starting the round");
        mModel.announceNewRound();

        mModel.getBot().whisperLink(mModel.getGiver(),extBindAddr, mModel.getSiteController().generatePW()); // send link
        mModel.setTimeStamp();

        while(mModel.getGameState() == GameState.WaitingForGiver){
            Date d = mModel.getTimeStamp();
            if(Util.diffTimeStamp(d,new Date()) > 20){
                mModel.incMissedOffer();
                mModel.announceGiverNotAccepted(mModel.getGiver());
                mModel.setGiver("");
                mModel.setGameState(GameState.GameStarted.Registration);
                //mModel.clear();
                mModel.clearRegisteredPlayers();
                break;
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mModel.setTimeStamp();
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
        Log.trace("userSize:  " + users.size());
        int index = rand.nextInt(users.size());
        String newGiver =  users.get(index);
        mModel.setGiver(newGiver);
    }

    private void sleepThread(int i){
        try {
            //change this to 30 sec.
            Log.info("Control sleeps for " + i + " seconds...");
            mModel.notifyRegistrationTime();
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * sorgt dafür, dass die command queue abgearbeitet wird.
     */
    private void processNextCommand(){
        for (; ; ) {
            Command c = mModel.pollNextCommand();
            try {
                if(c.validate()) {
                    Log.info(c.toString()+ " Command received!");
                    c.execute();
                }
            }catch(NullPointerException n){
                try {

                    //Log.db("No commands to be processed. sleeping...");
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
