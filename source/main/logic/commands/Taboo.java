package logic.commands;

import model.GameModel;

/**
 * Created by Marc on 05.04.2017.
 */
public class Taboo extends Command {

    public Taboo(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {
        //TODO update list of taboo suggestions
    }

    @Override
    public boolean validate() {
        return false;
        //anything to do here?
    }
}