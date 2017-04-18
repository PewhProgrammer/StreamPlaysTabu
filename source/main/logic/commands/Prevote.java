package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Tim on 18.04.2017.
 */
public class Prevote extends Command {

    private int id;

    public Prevote(GameModel gm, String ch, int id) {
        super(gm, ch);
        this.id = id;
    }

    @Override
    public void execute() {
        gameModel.prevote(id);
    }

    @Override
    public boolean validate() {
        return gameModel.getGameState().equals(GameState.Registration);
    }
}
