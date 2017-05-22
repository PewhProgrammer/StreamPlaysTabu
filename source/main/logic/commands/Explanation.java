package logic.commands;

import common.Util;
import model.GameModel;
import model.GameState;

import java.util.Date;

/**
 * Created by Marc on 04.04.2017.
 */
public class Explanation extends Command {

    private String explanation;
    private String user;

    public Explanation(GameModel gm, String ch, String explanation, String user) {
        super(gm, ch);
        this.explanation = explanation;
        this.user = user;
    }


    @Override
    public void execute() {
        gameModel.getBot().sendChatMessage("Explanation given: " + explanation);
        gameModel.addExplanation(explanation);
    }

    @Override
    public boolean validate() {

        if (!Util.checkCheating(explanation, gameModel)) {
            gameModel.getNeo4jWrapper().increaseUserError(gameModel.getGiver(), thisChannel);
            if (gameModel.getNeo4jWrapper().getUserError(gameModel.getGiver(), thisChannel) > 3) {
                gameModel.getNeo4jWrapper().setUserErrorTimeStamp(gameModel.getGiver(), new Date());
            }
            if (!Util.checkCheating(explanation, gameModel)) {
                gameModel.getNeo4jWrapper().increaseUserError(gameModel.getGiver(), thisChannel);
                if (gameModel.increaseErrCounter() == 2) {
                    gameModel.setGameState(GameState.Kick);
                    gameModel.getSiteController().sendError("We found again an invalid input. Round is over now");

                } else {
                    gameModel.getSiteController().sendError("Found invalid explanation. Please don't use your taboo or explain word or any character that is no letter, number or -,'");
                }
                return false;
            }
            return false;
        }

        return gameModel.getGameState().equals(GameState.GameStarted);
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Explanation e = (Explanation) o;

        return explanation.equals(e.getExplanation());
    }
}
