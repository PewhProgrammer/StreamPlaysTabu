package logic.commands;

import common.Log;
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
        Log.info("Registered user: " + user);
        gameModel.register(user);
    }

    @Override
    public boolean validate() {
        return gameModel.getGameState().equals(GameState.Registration);
    }

    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Register r = (Register) o;

        return user.equals(r.getUser());
    }
}
