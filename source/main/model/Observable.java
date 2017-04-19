package model;

import javafx.application.Platform;

import java.util.ArrayList;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public abstract class Observable {

    public ArrayList<IObserver> listIObserver = new ArrayList<>();

    /** TODO mehrere notify methoden */

    public void notifyQandA(){
        for (IObserver ob: listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifiyQandA();
                }
            });
        }
    }

    public void notifyCategoryChosen() {
        for (IObserver ob: listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifyCategoryChosen();
                }
            });
        }
    }

    public void notifyGameState() {
        for (IObserver ob : listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifyGameState();
                }
            });
        }
    }

    public void notifyExplanation() {
        for (IObserver ob : listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifyExplanation();
                }
            });
        }
    }

    public void notifyWinner() {
        for (IObserver ob : listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifyWinner();
                }
            });
        }
    }

    public void notifyGuess() {
        for (IObserver ob : listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifyGuess();
                }
            });
        }
    }
    

    public void notifyScoreUpdate() {
        for (IObserver ob : listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifyScoreUpdate();
                }
            });
        }
    }

    public void notifyGameMode() {
        for (IObserver ob : listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifyGameMode();
                }
            });
        }
    }

    public void notifyKick() {
        for (IObserver ob : listIObserver) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ob.onNotifyKick();
                }
            });
        }
    }

    public void notifyRegistrationTime() {
        for (IObserver ob : listIObserver) {
            ob.onNotifyRegistrationTime();
        }
    }

    public void addObserver(IObserver ob){
        listIObserver.add(ob);
    }

    public void removeObserver(IObserver ob){
        listIObserver.remove(ob);
    }
}
