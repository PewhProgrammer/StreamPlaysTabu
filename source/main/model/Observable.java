package model;

import java.util.ArrayList;

public abstract class Observable {

    private ArrayList<IObserver> listIObserver = new ArrayList<>(2);

    public void notifyQandA(){

        for (IObserver ob : listIObserver) {
            ob.onNotifyQandA();
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
    

    public void notifyScoreUpdate() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyScoreUpdate();
        }
    }

    public void notifyGameMode() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyGameMode();
        }
    }

    public void notifyKick() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyKick();
        }
    }

    public void notifyRegistrationTime() {
        for (IObserver ob : listIObserver) {
                ob.onNotifyRegistrationTime();
        }
    }

    public void notifyExplainWord() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyExplainWord();
        }
    }

    public void notifyTabooWords() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyTabooWords();
        }
    }

    public void notifyUpdateTime() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyUpdateTime();
        }
    }

    public void notifyUpdateTimerText(String s) {
        for (IObserver ob : listIObserver) {
            ob.onNotifyTimerText(s);
        }
    }

    public void addObserver(IObserver ob){
        listIObserver.add(0,ob);
    }

    public void updateObserver(IObserver ob){
        listIObserver.remove(1);
        listIObserver.add(1, ob);
    }

    public void removeObserver(IObserver ob){
        listIObserver.remove(ob);
    }
}
