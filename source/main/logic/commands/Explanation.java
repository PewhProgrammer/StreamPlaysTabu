package logic.commands;

import common.Util;
import logic.bots.AltTwitchBot;
import model.GameModel;
import model.GameState;

import java.util.Date;
import java.util.HashMap;

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

        String msg = "Explanation given: " + explanation;
        HashMap<String, AltTwitchBot> hostBots = gameModel.getHostBots();

        for (AltTwitchBot ab : hostBots.values()) {
            ab.sendChatMessage(msg);
        }

        gameModel.getBot().sendChatMessage(msg);
        gameModel.addExplanation(explanation);
    }

    @Override
    public boolean validate() {

        if (gameModel.getExplanations().isEmpty()) {
            return true;
        }

        String rtn = Util.checkCheating(explanation, gameModel);

        if (!rtn.equals("") && !gameModel.getExplanations().isEmpty()) {
            gameModel.getNeo4jWrapper().increaseUserError(gameModel.getGiver(), thisChannel);
            if (gameModel.getNeo4jWrapper().getUserError(gameModel.getGiver(), thisChannel) > 3) {
                gameModel.getNeo4jWrapper().setUserErrorTimeStamp(gameModel.getGiver(), new Date());
            }
            if (gameModel.increaseErrCounter() == 2) {
                gameModel.setGameState(GameState.Kick);
                gameModel.getSiteController().sendError(rtn + " Round is over now");

            } else {
                gameModel.getSiteController().sendError(rtn + " Please don't use your taboo or explain word or any character that is no letter, number or -,'");
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

    @Override
    public String toString(){
        return "Explanation["+explanation+"] from "+ user;
    }
}
