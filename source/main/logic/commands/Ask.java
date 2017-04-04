package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Ask extends Command {

    public Ask(GameModel gm, String ch) {
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
