package logic.commands;

import common.Log;
import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 05.04.2017.
 */
public class GiverJoined extends Command {

    public GiverJoined(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {
        Log.info("Giver has accepted the offer");
        gameModel.setGameState(GameState.GameStarted);
        //TODO taboo + validate von zwischenschirm in db
    }

    @Override
    public boolean validate() {
        if (!gameModel.getGameState().equals(GameState.WaitingForGiver)) {
            return false;
        }
        //TODO timelimit
        return false;
    }
}
