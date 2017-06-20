package logic.bots;

import common.Log;
import logic.commands.*;
import model.GameModel;
import model.GameState;
import org.jibble.pircbot.PircBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;


public class AltTwitchBot extends Bot {

    private Pirc bot;
    private String sender;
    private String feedback = "Thank you for your validation!";

    public AltTwitchBot(GameModel gm, String channel) {
        super(gm, channel);
        connectToChatroom(channel);
        new Thread() {
            @Override
            public void run() {
                Log.info("Starting new thread for validations processing...");
                processValidation();
                Log.info("Canceled Validation processing");
            }

        }.start();
    }

    private class Pirc extends PircBot {

        GameModel model;

        Pirc(String user, GameModel gm) {

            this.setName(user);
            this.setLogin("[" + user + "]");
            this.model = gm;
        }

        public void onMessage(String channel, String sender,
                              String login, String hostname, String message) {

            if (sender.equals("streamplaystaboo") | ("#" + sender).equals(channel)) {
                if (message.startsWith("!shutdown")) {
                    if (model.getGiverChannel().equals(sender)) {
                        System.exit(1);
                    }
                    Log.debug("shutdown command received!");
                    model.pushCommand(new Host(model, channel, channel));
                    partChannel(sender);
                    disconnect();
                    dispose();
                }
            }

            Command cmd = parseLine(message, sender);
            if (cmd != null) {
                model.pushCommand(cmd);
            }
        }

        public void onPrivateMessage(String sender, String login, String hostname, String message) {

            if (model.getHosts().contains(hostname)) {
                return;
            }

            if (model.getGiverChannel().equals("streamplaystaboo")) {
                System.out.println(message);
                String[] channel = message.split(" ");

                model.pushCommand(new Host(model, channel[0], channel[0], new AltTwitchBot(model, "#" + channel[0])));
            }
        }
    }

    @Override
    public void run() {

    }

    public static boolean checkChannelExist(String ch) {
        TwitchAPIRequester requester;
        requester = new TwitchAPIRequester();

        try {
            return requester.requestChannel(ch);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void connectToChatroom(String user) {
        bot = new Pirc("streamplaystaboo", this.model);
        bot.setVerbose(true);

        // Connect to the IRC server.
        try {
            bot.connect("irc.chat.twitch.tv",
                    6667,
                    "oauth:" + "ksfaxec4iil2ao18nf2d91ua9she0z"); //streamplaystaboo
            bot.joinChannel(user);
        } catch (Exception e) {
            bot.dispose();
        }

        Log.info("connected to " + user);
    }

    @Override
    public void sendChatMessage(String msg) {
        bot.sendMessage(channel, msg);
    }

    private void sendPrivMessage(String msg, String user) {
        sendChatMessage("/w " + user + " " + msg);
    }

    @Override
    public void whisperRules(String user) {
        sendChatMessage("/w " + user + " " +
                "Rules: 1. Every round, one person tries to explain a certain word. " +
                "2. This person constructs explanations while considering given sentence templates on an external webpage. " +
                "3. The other players in can !ask questions and try to !guess the word " +
                "4. The first one to !guess the word will be the winner of the round. Both, the guesser and giver, will be given points." +
                "5. Players can kicked with !votekick if they do not follow the rules. ");
    }

    @Override
    public void whisperLink(String user, String link, int pw) {
        sendPrivMessage("You are the giver! You will need to be on our webpage to give explanations and answer questions. Here is your link: " + link + "?pw=" + pw, user);
    }

    @Override
    public void announceNewRound() {
        sendChatMessage("------------------------------------------------------------------" +
                " A new round has started. Good Luck!!!" +
                " ------------------------------------------------------------------");
    }

    @Override
    public void announceWinner(String user) {
        sendChatMessage("The Winner is " + user + ". Congratulations!");
    }

    public void announceNoWinner() {
        sendChatMessage("There is no Winner!! Next time :)");
    }

    @Override
    public void announceGiverNotAccepted(String user) {
        sendChatMessage(user + " did not accept his offer to explain the word.");
    }

    @Override
    public void announceRegistration() {
        sendChatMessage("" +
                "------------------------------------------------- A new round will start soon. Type !register to get into the giver pool! -------------------------------------------------");
        sendQuestion();
    }

    @Override
    public void announceScore(String user, int score) {
        sendChatMessage(user + " You have " + score + " Points!");
    }

    @Override
    public List<String> getUsers(String channel) {

        List<String> users = new LinkedList<>();

        JSONObject obj = TwitchAPIRequester.requestUsers(channel);
        if (obj == null) {
            Log.error("no users");
            return users;
        }

        JSONObject chatters = obj.getJSONObject("chatters");
        JSONArray viewers = chatters.getJSONArray("viewers");
        JSONArray mods = chatters.getJSONArray("moderators");

        for (int i = 0; i < mods.length(); i++) {
            if (!mods.getString(i).equals("streamplaystaboo")) {
                users.add(mods.getString(i));
                Log.trace(mods.getString(i));
            }
        }

        for (int i = 0; i < viewers.length(); i++) {
            if (!viewers.getString(i).equals("streamplaystaboo")) {
                users.add(viewers.getString(i));
                Log.trace(viewers.getString(i));
            }
        }
        System.out.println("Found " + users.size() + " users in channel " + channel);
        return users;
    }

    private Command parseLine(String message, String sender) {
        this.sender = sender;
        return parseLine(message);
    }

    @Override
    public Command parseLine(String message) {
        String channel = this.channel.replaceAll("#", "");
        String[] parts = message.split(" ");

        // !register
        if (parts[0].equals("!register")) {
            return new Register(model, channel, sender);
        }

        // !guess
        if (parts[0].equals("!guess")) {
            String guess[] = message.split("!guess ");
            return new Guess(model, channel, sender, guess[1]);
        }

        // !ask
        if (parts[0].equals("!ask")) {
            String[] question = message.split("!ask ");
            return new Ask(model, channel, sender, question[1]);
        }

        // !answer
        if (parts[0].equals("!answer")) {
            String[] qAnda = message.split("!answer ");
            qAnda = qAnda[1].split("(->)+");
            return new Answer(model, channel, qAnda[0], qAnda[1]);
        }

        // !explain
        if (parts[0].equals("!explain")) {
            String[] qAnda = message.split("!explain ");
            return new Explanation(model, channel, qAnda[1], sender);
        }

        // !rules
        if (parts[0].equals("!rules")) {
            return new Rules(model, channel, sender);
        }

        // !score
        if (parts[0].equals("!score")) {
            return new Rank(model, channel, sender);
        }

        // !votekick
        if ((parts[0].equals("!votekick") && parts.length == 1) || (parts.length == 2 && parts[1].equals(model.getGiver()))) {
            return new Votekick(model, channel, sender);
        }

        // !streamerExplains
        if (parts[0].equals("!streamerExplains")) {
            return new StreamerExplains(model, channel, sender);
        }

        // !validate
        if (parts[0].equals("!validate")) {
            int ID = 0, valScore;
            try {
                valScore = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                return new ChatMessage(model, channel, sender, message);
            }
            try {
                ID = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                Command c = new Validate(model, channel, parts[1], valScore, sender);
                if (c.validate()) {
                    if ((feedback.length() + sender.length()) >= 500) {
                        sendChatMessage(feedback);
                        feedback = "Thank you for your validation!";
                    }
                    return c;
                }
            }
                if ((ID == 1) || (ID == 2) || (ID == 3) || (ID == 4) || (ID == 5)) {
                    Command c = new Validate(model, channel, ID, valScore, sender);
                    if (c.validate()) {
                        if ((feedback.length() + sender.length()) >= 500) {
                            sendChatMessage(feedback);
                            feedback = "Thank you for your validation!";
                        }
                        if (!feedback.contains(sender)) {
                            if (feedback.length() <= 30)
                                feedback = feedback + " " + sender;
                            else
                                feedback = feedback + ", " + sender;
                        }
                    }
                    return c;
                }
            }

            // !taboo
            if (parts[0].equals("!taboo")) {
                return new Taboo(model, channel, parts[1], sender);
            }

            // !vote
            if (parts[0].equals("!vote")) {
                int[] preVotes = new int[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    try {
                        int vote = Integer.parseInt(parts[i]);
                        preVotes[i - 1] = vote;
                    } catch (NumberFormatException e) {
                    }
                }
                return new Prevote(model, channel, preVotes, sender);
            }

            return new ChatMessage(model, channel, sender, message);
        }

    private void processValidation() {
        for (; ; ) {

            try {
                if (feedback.length() == 30)
                    Thread.sleep(10000);
                else {
                    sendChatMessage(feedback);
                    feedback = "Thank you for your validation!";
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}

