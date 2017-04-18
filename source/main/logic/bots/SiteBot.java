package logic.bots;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

/**
 * Created by Marc on 03.04.2017.
 */
public class SiteBot {

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    public SiteBot() {
        //connect to our server for the external webpage.
        try {
            sock = new Socket("", 1234); //TODO enter our serverAdress and Port (look at TwitchBot)!
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendQuestion(String question) {
        //TODO send !ask question
    }

    public void sendWord(String word, Set<String> tabooWords) {
        //TODO send word2explain + taboowords for giver
    }

    //TODO method for starting Thread that listens to server
}
