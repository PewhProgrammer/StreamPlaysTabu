package logic.bots;

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

    final String accessToken = "k5ye8k6cwa3gifz451u4terjkl82a5"; //TODO get twitch account + token for our bot!

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
            out.println("NICK k3uleeeBot"); //TODO change to our Botname!

            String serverResponse;
            for(int i = 0; i < 7; i++) {
                if((serverResponse = in.readLine()) != null)
                    System.out.println("> " +serverResponse);
            }

            System.out.println("Connected to Twitchserver");

            connectToChatroom(channel);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        //TODO sleep until connected to chatroom, multithreading stuff (locks, wait, notify etc.)

        while (joined) {

            String line = "";

            try {
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line.startsWith("PING")) {
                out.println("PONG"+" "+ line.substring(5)+ "\r\n");
                continue;
            }

            String[] message = line.split(" ");
            String sender = message[0].substring(1).split("!")[0];

            if (message[1].equals("PRIVMSG") && message.length > 3 && message[3].startsWith("!")) {

                // !register
                if (message[3].equals("!register")) {
                    Command cmd = new Register(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !guess
                if (message[3].equals("!guess")) {
                    Command cmd = new Guess(model, channel, sender, message[4]);
                    model.pushCommand(cmd);
                }

                // !ask
                if (message[3].equals("!ask")) {
                    Command cmd = new Ask(model, channel, message[4]);
                    model.pushCommand(cmd);
                }

                // !rules
                if (message[3].equals("!rules")) {
                    Command cmd = new Rules(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !score
                if (message[3].equals("!score")) {
                    Command cmd = new Rank(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !votekick
                if (message[3].equals("!votekick")) {
                    Command cmd = new Votekick(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !streamerexplains
                if (message[3].equals("!streamerexplains")) {
                    Command cmd = new StreamerExplains(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !validate
                if (message[3].equals("!validate")) {
                    int ID = Integer.parseInt(message[4]);
                    int valScore = Integer.parseInt(message[5]);
                    Command cmd = new Validate(model, channel, ID, valScore);
                    model.pushCommand(cmd);
                }

                // !taboo
                if (message[3].equals("!taboo")) {
                    Command cmd = new Taboo(model, channel, message[4]);
                    model.pushCommand(cmd);
                }

                // !vote
                if (message[3].equals("!vote")) {
                    int voteNum = Integer.parseInt(message[4]);
                    Command cmd = new Prevote(model, channel, voteNum);
                    model.pushCommand(cmd);
                }





            }
            if (message[1].equals("PRIVMSG") && message.length > 4 && message[4].startsWith("!")) {

                // !register
                if (message[4].equals("!register")) {
                    Command cmd = new Register(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !guess
                if (message[4].equals("!guess")) {
                    Command cmd = new Guess(model, channel, sender, message[5]);
                    model.pushCommand(cmd);
                }

                // !ask
                if (message[4].equals("!ask")) {
                    Command cmd = new Ask(model, channel, message[5]);
                    model.pushCommand(cmd);
                }

                // !rules
                if (message[4].equals("!rules")) {
                    Command cmd = new Rules(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !score
                if (message[4].equals("!score")) {
                    Command cmd = new Rank(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !votekick
                if (message[4].equals("!votekick")) {
                    Command cmd = new Votekick(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !streamerexplains
                if (message[4].equals("!streamerexplains")) {
                    Command cmd = new StreamerExplains(model, channel, sender);
                    model.pushCommand(cmd);
                }

                // !validate
                if (message[4].equals("!validate")) {
                    int ID = Integer.parseInt(message[5]);
                    int valScore = Integer.parseInt(message[6]);
                    Command cmd = new Validate(model, channel, ID, valScore);
                    model.pushCommand(cmd);
                }

                // !taboo
                if (message[4].equals("!taboo")) {
                    Command cmd = new Taboo(model, channel, message[5]);
                    model.pushCommand(cmd);
                }

                // !vote
                if (message[4].equals("!vote")) {
                    int voteNum = Integer.parseInt(message[5]);
                    Command cmd = new Prevote(model, channel, voteNum);
                    model.pushCommand(cmd);
                }

            }

        }

    }
    //TODO
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
                    } else System.out.println("TwitchBot: > " + serverResponse);
            }

            System.out.println("TwitchBot: Accessed " + channel + "Chatroom");
            out.println("PRIVMSG #" + channel + "Hallooooo");

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
