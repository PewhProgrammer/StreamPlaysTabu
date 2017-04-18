package gui;

import model.IObserver;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class GuiController implements IObserver {

    @Override
    public void onNotify() {
        //something interested happened to gameModel
    }

    @Override
    public void onNotifyGameState() {

    }

    @Override
    public void onNotifiyQandA() {

    }

    @Override
    public void onNotifyCategoryChosen() {

    }

    @Override
    public void onNotifyExplanation() {

    }

    @Override
    public void onNotifyWinner() {

    }

    @Override
    public void onNotifyGuess() {

    }

    @Override
    public void onNotifyStartGame() {

    }

    @Override
    public void onNotifyWaitingForPlayers() {

    }
}
