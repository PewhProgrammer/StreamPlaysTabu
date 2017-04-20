package logic.commands;

import common.Log;
import model.GameModel;
import model.GameState;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marc on 05.04.2017.
 */
public class GiverJoined extends Command {

    private static final int TIME_DIFFERENCE = 1000 * 30;

    public GiverJoined(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {
        Log.info("Giver has accepted the offer");
        gameModel.setGameState(GameState.GameStarted);
        gameModel.setTimeStamp();
        gameModel.transferTabooSuggestions();
        //TODO validate von zwischenschirm in db
    }

    @Override
    public boolean validate() {
        if (!gameModel.getGameState().equals(GameState.WaitingForGiver)) {
            return false;
        }

        Date joinedTime = new Date();
        Date referenceTime = gameModel.getTimeStamp();
        long diff = joinedTime.getTime() - referenceTime.getTime();
        if (diff > TIME_DIFFERENCE) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }
        return true;
    }
}
