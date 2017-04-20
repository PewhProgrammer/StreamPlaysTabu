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

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Prevote p = (Prevote) o;

        return id == p.getId();
    }
}
