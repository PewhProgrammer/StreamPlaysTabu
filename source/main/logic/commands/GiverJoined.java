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
        //TODO send explain+taboowords, how to start round counter?
        //update model + observer -> gamescreen
    }

    @Override
    public boolean validate() {
        //TODO timelimit, game state
        return false;
    }
}
