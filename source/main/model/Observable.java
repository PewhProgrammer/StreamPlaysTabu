package model;

import java.util.ArrayList;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public abstract class Observable {

    public ArrayList<IObserver> listIObserver = new ArrayList<>();

    /** TODO mehrere notify methoden */

    public void notifyUpdate(){
        for (IObserver ob: listIObserver) {
            ob.onNotify();
        }
    }

    public void notifyWaitingForPlayers(){
        for (IObserver ob: listIObserver) {
            ob.onNotify();
        }
    }

    public void notifyStartGame(){
        for (IObserver ob: listIObserver) {
            ob.onNotify();
        }
    }

    public void addObserver(IObserver ob){
        listIObserver.add(ob);
    }

    public void removeObserver(IObserver ob){
        listIObserver.remove(ob);
    }
}
