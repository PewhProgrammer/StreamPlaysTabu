package logic.bots;

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

    boolean terminate = false;

    public abstract void connectToChatroom(String user);

    public abstract void sendChatMessage(String msg);

    public abstract void whisperRules(String user);

    public abstract void whisperLink(String user, String link);

    public abstract void announceNewRound();

    public abstract void announceWinner();

    public abstract void announceRegistration();
}
