package logic.bots;

import logic.commands.Command;
import model.GameModel;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created by Marc on 03.04.2017.
 */
public abstract class Bot {

    private final static String QUEST_MSG = "Do you like to play StreamPlaysTaboo? Tell us how we can improve! https://goo.gl/forms/ED5savZgNQEOPES82";

    protected Socket sock;
    protected PrintWriter out;
    protected BufferedReader in;
    protected boolean joined = false;
    protected GameModel model;
    protected String channel;

    public Bot(GameModel gm, String channel) {
        this.model = gm;
        this.channel = channel;
    }

    protected final String rules = "rules!";

    boolean terminate = false;

    public abstract void run();

    public abstract void connectToChatroom(String user);

    public abstract void sendChatMessage(String msg);

    public abstract void whisperRules(String user);

    public abstract void whisperLink(String user, String link, int pw);

    public abstract void announceNewRound();

    public abstract void announceWinner(String user);

    public abstract void announceNoWinner(String word);

    protected void sendQuestion() {
        sendChatMessage(QUEST_MSG);
    }

    public abstract void announceGiverNotAccepted(String user);

    public abstract void announceRegistration();

    public abstract void announceScore(String user, int score);

    public abstract List<String> getUsers(String user);

    public abstract Command parseLine(String line);

}
