package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 04.04.2017.
 */
public class Rank extends Command {

    private String user;

    public Rank(GameModel gm, String ch, String user) {
        super(gm, ch);
        this.user = user;
    }

    @Override
    public void execute() {
        gameModel.getBot().announceScore(user, gameModel.getScore(user));
    }

    @Override
    public boolean validate() {
        return true;
    }
}
