package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 05.04.2017.
 */
public class GiverLeft extends Command {

    public GiverLeft(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {
        gameModel.setGameState(GameState.GameClosed);
        //update model + observer ( -> registration screen)
        //TODO reset round specific information: #players, qAndA, explanations, category, etc.
    }

    @Override
    public boolean validate() {
        //anything to do here?
        return false;
    }
}
