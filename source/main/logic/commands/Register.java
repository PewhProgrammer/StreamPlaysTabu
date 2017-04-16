package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Register extends Command {

    public Register(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {
        //TODO add to registered set, update #players and game state
    }

    @Override
    public boolean validate() {
        return false;
        //TODO check game state
    }
}
