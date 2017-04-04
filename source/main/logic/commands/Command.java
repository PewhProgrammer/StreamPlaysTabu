package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public abstract class Command {

    private GameModel gameModel;
    private String thisChannel; //channel you're working on.
    boolean needsAdmin = false;

    public Command(GameModel gm, String ch) {
        gameModel = gm;
        thisChannel = ch;
    }

    public abstract void execute();

    public abstract boolean validate();

    public boolean isAdminNeeded() {
        return needsAdmin;
    }
}
