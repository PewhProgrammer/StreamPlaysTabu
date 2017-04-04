package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 04.04.2017.
 */
public class Rank extends Command {

    public Rank(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}
