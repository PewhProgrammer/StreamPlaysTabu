package logic.commands;

import common.Log;
import common.Util;
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
        this.guess = guess.toLowerCase();
    }

    @Override
    public void execute() {

        Log.trace("Guess received: " + guess);
        if (Util.guessEquals(guess,gameModel.getExplainWord())) {
            gameModel.win(name,getChannel());
            gameModel.generateVotingCategories();
            gameModel.setGameState(GameState.Win);
            Log.trace("Winner found: "+name);
        } else {
            gameModel.guess(guess);
        }
    }

    @Override
    public boolean validate() {

        if (!gameModel.getGameState().equals(GameState.GameStarted)) {
            return false;
        }

        if (name.equals(gameModel.getGiver())) {
            //TODO bestrafen
            return false;
        }

        return true;
    }

    public String getUser() {
        return name;
    }

    public String getGuess() {
        return guess;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Guess g = (Guess) o;

        return name.equals(g.getUser()) && guess.equals(g.getGuess()) && thisChannel.equals(g.getChannel());
    }
}
