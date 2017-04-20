package logic.bots;

import common.Log;
import logic.commands.*;
import model.GameModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Marc on 03.04.2017.
 */
public class TwitchBot extends Bot {

    final String accessToken = "o63lke534mk921wdqawn62bqetfovb"; //TODO get twitch account + token for our bot!

    public TwitchBot(GameModel model, String channel) {

        this.model = model;
        this.channel = channel;

        //Setup socket etc. and connect to Twitchserver
        try {
            sock = new Socket("irc.chat.twitch.tv", 6667);

            out = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            //login
            out.println("PASS oauth:" + accessToken);
            out.println("NICK streamplaystaboo"); //TODO change to our Botname!

            String serverResponse;
            for(int i = 0; i < 7; i++) {
                if((serverResponse = in.readLine()) != null)
                    System.out.println("> " +serverResponse);
            }

            Log.info("Connected to Chatroom");

            connectToChatroom(channel);

            Thread mTHREAD = new Thread() {
                @Override
                public void run() {
                    TwitchBot.this.run();
                }

            } ;

            mTHREAD.start();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        //TODO sleep until connected to chatroom, multithreading stuff (locks, wait, notify etc.)

        while (joined) {
            Log.info("while entered");
            String line = "";

            try {
                line = in.readLine();
                Log.info("read line");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (line.startsWith("PING")) {
                out.println("PONG"+" "+ line.substring(5)+ "\r\n");
                continue;
            }

            Command cmd = parseLine(line,"");
            if (cmd != null) {
                model.pushCommand(cmd);
            }
        }

        Log.info("Twitchbot exited");
    }

    @Override
    public Command parseLine(String line, String filler) {

        String[] message = line.split(" ");
        String sender = message[0].substring(1).split("!")[0];

        if (message[1].equals("PRIVMSG") && message.length > 3 && message[3].startsWith(":!")) {

            // !register
            if (message[3].equals(":!register")) {
                Log.info("Register Command received");
                return new Register(model, channel, sender);
            }

            // !guess
            if (message[3].equals(":!guess")) {
                return new Guess(model, channel, sender, message[4]);
            }

            // !ask
            if (message[3].equals(":!ask")) {
                String[] question = line.split("!ask ");
                return new Ask(model, channel, question[1]) ;
            }

            // !rules
            if (message[3].equals(":!rules")) {
                return new Rules(model, channel, sender);
            }

            // !score
            if (message[3].equals(":!score")) {
                return new Rank(model, channel, sender);
            }

            // !votekick
            if (message[3].equals(":!votekick")) {
                return new Votekick(model, channel, sender);
            }

            // !streamerexplains
            if (message[3].equals(":!streamerexplains")) {
                return new StreamerExplains(model, channel, sender);
            }

            // !validate
            if (message[3].equals(":!validate")) {
                int ID = Integer.parseInt(message[4]);
                int valScore = Integer.parseInt(message[5]);
                return new Validate(model, channel, ID, valScore);
            }

            // !taboo
            if (message[3].equals(":!taboo")) {
                return new Taboo(model, channel, message[4]);
            }

            // !vote
            if (message[3].equals(":!vote")) {
                int voteNum = Integer.parseInt(message[4]);
                return new Prevote(model, channel, new int[3]);
            }
        }

        if (message[1].equals("PRIVMSG") && message.length > 4 && message[4].startsWith(":!")) {

            // !register
            if (message[4].equals(":!register")) {
                return new Register(model, channel, sender);
            }

            // !guess
            if (message[4].equals(":!guess")) {
                return new Guess(model, channel, sender, message[5]);
            }

            // !ask
            if (message[4].equals(":!ask")) {
                String[] question = line.split("!ask ");
                return new Ask(model, channel, question[1]);
            }

            // !rules
            if (message[4].equals(":!rules")) {
                return new Rules(model, channel, sender);
            }

            // !score
            if (message[4].equals(":!score")) {
                return new Rank(model, channel, sender);
            }

            // !votekick
            if (message[4].equals(":!votekick")) {
                return new Votekick(model, channel, sender);
            }

            // !streamerexplains
            if (message[4].equals(":!streamerexplains")) {
                return new StreamerExplains(model, channel, sender);
            }

            // !validate
            if (message[4].equals(":!validate")) {
                int ID = Integer.parseInt(message[5]);
                int valScore = Integer.parseInt(message[6]);
                return new Validate(model, channel, ID, valScore);
            }

            // !taboo
            if (message[4].equals("!taboo")) {
                return new Taboo(model, channel, message[5]);
            }

            // !vote
            if (message[4].equals(":!vote")) {
                int voteNum = Integer.parseInt(message[5]);
                return new Prevote(model, channel, new int[3]);
            }
        }
        return null;
    }


    @Override
    public void connectToChatroom(String user) {
        try {
            out.println("JOIN #" + channel);

            String serverResponse;
            for (int i = 0; i < 3; i++) {
                if ((serverResponse = in.readLine()) != null)
                    if (serverResponse.equals("PING :tmi.twitch.tv")) {
                        System.out.println("TwitchBot: > " + serverResponse);
                        out.println("PONG :tmi.twitch.tv");
                        i--;
                    }
                    else System.out.println("TwitchBot: > " + serverResponse);
            }

            System.out.println("TwitchBot: Accessed " + channel + " Chatroom");
            out.println("PRIVMSG #" + channel + " :Hallooooo");

            joined = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //TODO
    @Override
    public void disconnectFromChatroom(String user) {

    }

    @Override
    public void sendChatMessage(String msg) {
        out.println("PRIVMSG" + " " + "#" + channel + " :" + msg);
    }

    @Override
    public void whisperRules(String user) {
        out.println("PRIVMSG" + " " + "#" + channel + " :/w " + user + "rules" );
    }

    @Override
    public void whisperLink(String user, String link) {
        out.println("PRIVMSG" + " " + "#" + channel + " :/w " + user + "You are the giver! Here is your link, please click it!:" + link);
    }

    @Override
    public void announceNewRound() {
        out.println("PRIVMSG" + " " + "#" + channel + " :A new round has started. Good Luck!!!");
    }

    @Override
    public void announceWinner(String user) {
        out.println("PRIVMSG" + " " + "#" + channel + " :The Winner is " + user + "Congratulations!"); //PogChamp?
    }

    @Override
    public void announceRegistration() {
        out.println("PRIVMSG" + " " + "#" + channel + " :A new round will start soon. Type !register to get into the giver pool!");
    }

    @Override
    public void announceScore(String user, int score) {
        out.println ("PRIVMSG" + " " + "#" + channel + " :" + channel + ".You have" + score + "Points!");
    }

}
