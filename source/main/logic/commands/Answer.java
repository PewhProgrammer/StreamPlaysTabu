package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 04.04.2017.
 */
public class Answer extends Command {

    String question;
    String answer;

    public Answer(GameModel gm, String ch, String question, String answer) {
        super(gm, ch);
        this.question = question;
        this.answer = answer;
    }

    /**
     * Adds a question with corresponding answer to the gamemodel
     */
    @Override
    public void execute() {
        gameModel.addQAndA(question, answer);
    }

    @Override
    public boolean validate() {
        //TODO anti cheating mechanism

        if (!gameModel.getGameState().equals(GameState.GameStarted)) {
            return false;
        }

        return true;
    }
}
