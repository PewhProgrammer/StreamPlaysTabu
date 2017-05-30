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
        gameModel.clear();
        gameModel.setGameState(GameState.Registration);
    }

    @Override
    public boolean validate() {
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
