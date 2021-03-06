package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Tim on 18.04.2017.
 */
public class Prevote extends Command {

    private final int[] id;
    private final String sender;

    public Prevote(GameModel gm, String ch, int[] id, String sender) {
        super(gm, ch);
        this.id = id;
        this.sender = sender;
    }

    @Override
    public void execute() {
        for (int i = 0; i < id.length && i < 5; i++) {
            gameModel.prevote(id[i]-1, sender);
        }
    }

    @Override
    public boolean validate() {

        if (!gameModel.contribute(sender, thisChannel)) {
            return false;
        }

        for (int i = 0; i < id.length && i < 5; i++) {
            if(id[i] < 1 || id[i] >= gameModel.getPrevoteCategories().length) {
                return false;
            }
        }

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

        if (getId().length != reference.length) {
            return false;
        }

        for (int i = 0; i < id.length; i++) {
            if (id[i] != reference[i]) {
                return false;
            }
        }

        return true;
    }
}
