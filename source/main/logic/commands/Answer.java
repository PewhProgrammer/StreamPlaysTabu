package logic.commands;

import common.Log;
import common.Util;
import model.GameModel;
import model.GameState;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
        if (!gameModel.getGameState().equals(GameState.GameStarted)) {
            return false;
        }

        String rtn = Util.checkCheating(answer, gameModel);

        if (!rtn.equals("")) {
            gameModel.getNeo4jWrapper().increaseUserError(gameModel.getGiver(), thisChannel);
            if (gameModel.getNeo4jWrapper().getUserError(gameModel.getGiver(), thisChannel) > 3) {
                gameModel.getNeo4jWrapper().setUserErrorTimeStamp(gameModel.getGiver(), new Date());
            }
            if (gameModel.increaseErrCounter() == 2) {
                gameModel.setGameState(GameState.Kick);
                gameModel.getSiteController().sendError(rtn + " Round is over now!");

            } else {
                gameModel.getSiteController().sendError(rtn + " Please don't use your taboo or explain word or any character that is no letter, number or -,'");
            }
            return false;
        }
        return true;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Answer a = (Answer) o;

        return answer.equals(a.getAnswer()) && question.equals(a.getQuestion()) && thisChannel.equals(a.getChannel());
    }
}
