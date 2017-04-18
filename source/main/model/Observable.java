package model;

import java.util.ArrayList;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public abstract class Observable {

    public ArrayList<IObserver> listIObserver = new ArrayList<>();

    /** TODO mehrere notify methoden */

    public void notifyQandA(){
        for (IObserver ob: listIObserver) {
            ob.onNotifiyQandA();
        }
    }

    public void notifyCategoryChosen() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyCategoryChosen();
        }
    }

    public void notifyGameState() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyGameState();
        }
    }

    public void notifyExplanation() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyExplanation();
        }
    }

    public void notifyWinner() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyWinner();
        }
    }

    public void notifyGuess() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyGuess();
        }
    }

    public void notifyStartGame() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyStartGame();
        }
    }

    public void notifyWaitingForPlayers() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyWaitingForPlayers();
        }
    }

    public void addObserver(IObserver ob){
        listIObserver.add(ob);
    }

    public void removeObserver(IObserver ob){
        listIObserver.remove(ob);
    }
}
