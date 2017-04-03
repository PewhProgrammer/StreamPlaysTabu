package logic.bots;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Marc on 03.04.2017.
 */
public class TwitchBot extends Bot {

    final String accessToken = ""; //TODO get twitch account + token for our bot!

    public TwitchBot() {

        //Setup socket etc. and connect to Twitchserver
        try {
            sock = new Socket("irc.chat.twitch.tv", 6667);

            out = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            //login
            out.println("PASS oauth:" + accessToken);
            out.println("NICK k3uleeeBot"); //TODO change to our Botname!

            String serverResponse;
            for(int i = 0; i < 7; i++) {
                if((serverResponse = in.readLine()) != null)
                    System.out.println("> " +serverResponse);
            }

            System.out.println("Connected to Twitchserver");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectToChatroom(String user) {

    }

    @Override
    public void sendChatMessage(String msg) {

    }

    @Override
    public void whisperRules(String user) {

    }

    @Override
    public void whisperLink(String user, String link) {

    }

    @Override
    public void announceNewRound() {

    }

    @Override
    public void announceWinner() {

    }

    @Override
    public void announceRegistration() {

    }

}
