package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Tim on 18.04.2017.
 */
public class Prevote extends Command {

    private int[] id;

    public Prevote(GameModel gm, String ch, int[] id) {
        super(gm, ch);
        this.id = id;
    }

    @Override
    public void execute() {
        for (int i : id) {
            gameModel.prevote(i);
        }
    }

    @Override
    public boolean validate() {
        return gameModel.getGameState().equals(GameState.Registration);
    }

    public int[] getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Prevote p = (Prevote) o;
        int[] reference = p.getId();

        for (int i = 0; i < id.length; i++) {
            if (id[i] != reference[i]) {
                return false;
            }
        }

        return true;
    }
}
