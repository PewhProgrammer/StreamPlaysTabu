package logic.commands;

import logic.bots.Bot;
import model.GameModel;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marc on 03.04.2017.
 */
public class CommandFactory {

    private GameModel gameModel;
    private Bot bot; //differs between Twitch and Beam Bot
    private String thisChannel; //channel you're working on.

    Set<String> admins;

    public CommandFactory(GameModel gm, Bot b, String ch) {
        gameModel = gm;
        bot = b;
        thisChannel = ch;
        admins = new HashSet<String>();
    }

    public Command makeCommand(String str) {
        if(!str.startsWith("!")) return null;


        return null;
    }

    private boolean isCommand(String str) {

        return false;
    }

}
