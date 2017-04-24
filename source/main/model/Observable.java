package model;

import gui.FXMLController;
import javafx.application.Platform;

import java.util.ArrayList;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public abstract class Observable {

    public ArrayList<IObserver> listIObserver = new ArrayList<>(2);

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
            ob.onNotifyGuess(); //platform in der methode
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
        listIObserver.add(0,ob);
    }

    public void updateObserver(IObserver ob){
        if(ob instanceof FXMLController) {
            listIObserver.remove(0);
            listIObserver.add(0, ob);
            return;
        }
        listIObserver.remove(1);
        listIObserver.add(1, ob);

    }

    public void removeObserver(IObserver ob){
        listIObserver.remove(ob);
    }
}
