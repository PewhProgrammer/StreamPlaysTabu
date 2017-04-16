package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Guess extends Command {

    String name;
    String guess;

    /**
     *
     * @param gm gamemodel
     * @param ch channelname
     * @param name name of user who gave us the guess.
     * @param guess the guess.
     */
    public Guess(GameModel gm, String ch, String name, String guess) {
        super(gm, ch);
        this.name = name;
    }

    @Override
    public void execute() {
        //TODO check for right answer
        //TODO if right answer is given, inform observer and set gamestate to register, clear all game specific information,
            //TODO add guesses to db, update score of giver, winner and stream
        //TODO if not, update guess list and inform observer if top 10 has changed
    }

    @Override
    public boolean validate() {
        return false;
        //TODO check game state
    }
}
