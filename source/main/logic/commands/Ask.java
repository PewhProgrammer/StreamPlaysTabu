package logic.commands;

import logic.bots.Bot;
import logic.bots.SiteBot;
import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 03.04.2017.
 */
public class Ask extends Command {

    private String question;

    public Ask(GameModel gm, String ch, String question) {
        super(gm, ch);
        this.question = question;
    }

    /**
     * sends a question to the giver's page
     */
    @Override
    public void execute() {
        gameModel.getSiteBot().sendQuestion(question);
    }

    @Override
    public boolean validate() {
        if (!gameModel.getGameState().equals(GameState.GameStarted)) {
            return false;
        }
        //TODO bad word filter?
        return true;
    }
}
