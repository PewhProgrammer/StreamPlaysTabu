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

    /**Was soll die Methode machen? Wenn er den string parst um herauszufinden
    ob es ein Command ist, verlieren wir doch gerade die Information, um
    welchen Command es sich handelt. **/
    private boolean isCommand(String str) {

        return false;
    }

}
