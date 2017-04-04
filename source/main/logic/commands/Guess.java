package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Guess extends Command {

    String name;

    /**
     *
     * @param gm gamemodel
     * @param ch channelname
     * @param name name of user who gave us the guess.
     */
    public Guess(GameModel gm, String ch, String name) {
        super(gm, ch);
        this.name = name;
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}
