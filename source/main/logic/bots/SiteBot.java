package logic.bots;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

    //TODO method for starting Thread that listens to server
}
