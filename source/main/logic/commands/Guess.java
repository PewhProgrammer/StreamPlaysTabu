package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Guess extends Command {

    public Guess(GameModel gm, Bot b, String ch) {
        super(gm, b, ch);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}
