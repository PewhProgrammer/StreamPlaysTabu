package logic.bots;

import logic.commands.Command;
import model.GameModel;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Marc on 03.04.2017.
 */
public abstract class Bot {

    protected Socket sock;
    protected PrintWriter out;
    protected BufferedReader in;
    protected boolean joined = false;
    protected GameModel model;
    protected String channel;

    protected final String rules = "rules!";

    boolean terminate = false;

    public abstract void run();

    public abstract void connectToChatroom(String user);

    public abstract void disconnectFromChatroom(String user);

    public abstract void sendChatMessage(String msg);

    public abstract void whisperRules(String user);

    public abstract void whisperLink(String user, String link);

    public abstract void announceNewRound();

    public abstract void announceWinner(String user);

    public abstract void announceRegistration();

    public abstract void announceScore(String user, int score);

    public abstract Command parseLine(String line);
}
