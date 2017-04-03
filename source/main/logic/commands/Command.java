package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public abstract class Command {

    private GameModel gameModel;
    private Bot bot; //differs between Twitch and Beam Bot
    private String thisChannel; //channel you're working on.
    boolean needsAdmin = false;

    public Command(GameModel gm, Bot b, String ch) {
        gameModel = gm;
        bot = b;
        thisChannel = ch;
    }

    public abstract void execute();

    public abstract boolean validate();

    public boolean isAdminNeeded() {
        return needsAdmin;
    }
}
