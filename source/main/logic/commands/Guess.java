package logic.commands;

import logic.bots.Bot;
import model.GameModel;
import model.GameState;

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

        if (guess.equals(gameModel.getExplainWord())) {
            gameModel.win(name);
            gameModel.setGameState(GameState.Register);
            gameModel.clear();
        } else {
            gameModel.guess(guess);
        }

        //TODO if not, update guess list and inform observer if top 10 has changed, add guesses to db
    }

    @Override
    public boolean validate() {
        return false;
        //TODO check game state
    }
}
