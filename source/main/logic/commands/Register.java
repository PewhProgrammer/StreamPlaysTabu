package logic.commands;

import logic.bots.Bot;
import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 03.04.2017.
 */
public class Register extends Command {

    private String user;

    public Register(GameModel gm, String ch, String user) {
        super(gm, ch);
        this.user = user;
    }

    @Override
    public void execute() {
        gameModel.register(user);
    }

    @Override
    public boolean validate() {
        return gameModel.getGameState().equals(GameState.Registration);
    }
}
