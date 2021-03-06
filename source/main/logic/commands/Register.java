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
        //Log.info(user + " registered");
        gameModel.register(user);
    }

    @Override
    public boolean validate() {
        if(gameModel.getRegisteredPlayers().contains(user)) return false;
        if (!gameModel.contribute(user, thisChannel)) {
            return false;
        }
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

    @Override
    public String toString(){
        return "Register["+user+"]";
    }
}
