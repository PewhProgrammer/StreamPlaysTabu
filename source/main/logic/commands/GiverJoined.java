package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 05.04.2017.
 */
public class GiverJoined extends Command {

    private String giver;

    public GiverJoined(GameModel gm, String ch, String giver) {
        super(gm, ch);
    }

    @Override
    public void execute() {
        gameModel.setGameState(GameState.GameStarted);
        //TODO taboo + validate von zwischenschirm in db
    }

    @Override
    public boolean validate() {
        //TODO timelimit, game state
        return false;
    }
}
