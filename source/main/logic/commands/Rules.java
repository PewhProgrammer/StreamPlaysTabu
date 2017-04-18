package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Rules extends Command {

    private String user;

    public Rules(GameModel gm, String ch, String user) {
        super(gm, ch);
        this.user = user;
    }

    @Override
    public void execute() {
        gameModel.getBot().whisperRules(user);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
