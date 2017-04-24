package logic.bots;

import common.Log;
import logic.commands.*;
import model.GameModel;
import org.jibble.pircbot.PircBot;

/**
 * Created by Thinh-Laptop on 20.04.2017.
 */
public class AltTwitchBot extends Bot {

    private Pirc bot;
    private String sender;

    public AltTwitchBot(GameModel gm, String channel) {
        super(gm, channel);
        connectToChatroom(channel);

    }

    private class Pirc extends PircBot {

        public Pirc(String user) {
            this.setName(user);
            this.setLogin("[" + user + "]");
        }

        public void onMessage(String channel, String sender,
                              String login, String hostname, String message) {
            //sendMessage(channel,"@" + sender + " " +curse.get(r.nextInt(curse.size())));
            if (message.equalsIgnoreCase("PING")) {
                sendMessage(channel, "PONG");
            }

            Command cmd = parseLine(message, sender);
            if (cmd != null) {
                model.pushCommand(cmd);

                if (sender.equals("pewhtv")) {
                    if (message.startsWith("!print")) {
                        Log.debug("Print command received!");
                        sendMessage(channel, String.format("@" + sender + ": Ping! I've counted %d occurences on twitch! Kappa"));
                    }
                    if (message.startsWith("!shutdown")) {
                        Log.debug("Shutdown command received!");
                        sendMessage(channel, "Ping! I'm going offline!!");
                        System.exit(1);
                    }
                }
            }
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void connectToChatroom(String user) {
        bot = new Pirc("streamplaystaboo");

        // Connect to the IRC server.
        try {
            bot.connect("irc.chat.twitch.tv",
                    6667,
                    "oauth:" + "ksfaxec4iil2ao18nf2d91ua9she0z"); //streamplaystaboo
        } catch (Exception e) {
            Log.trace("HTTPResponse bot connection failure");
            System.exit(1);
        }

        bot.joinChannel(user);
        Log.info("connected");
    }

    @Override
    public void disconnectFromChatroom(String user) {

    }

    @Override
    public void sendChatMessage(String msg) {
        bot.sendMessage(channel, msg);

    }

    public void sendPrivMessage(String msg,String user){
        sendChatMessage("/w "+user+" " +msg);
    }

    @Override
    public void whisperRules(String user) {
        sendChatMessage("/w " + user + " Rules");
    }

    @Override
    public void whisperLink(String user, String link) {
        //sendChatMessage(" " + user + " You are the giver! Here is your link, please click it! " + link);
        //sendPrivMessage("You are the giver! Here is your link, please click it! " + link,user);
        sendPrivMessage("Your Explain word: " + model.getExplainWord(),user);
        model.getSiteBot().onGiverJoined();
    }

    @Override
    public void announceNewRound() {
        sendChatMessage("A new round has started. Good Luck!!!");
    }

    @Override
    public void announceWinner(String user) {
        sendChatMessage("The Winner is " + user + "Congratulations!"); //PogChamp?
    }

    public void announceNoWinner() {
        sendChatMessage("There is no Winner!! Next time :)"); //PogChamp?
    }

    @Override
    public void announceRegistration() {
        sendChatMessage("A new round will start soon. Type !register to get into the giver pool!");
    }

    @Override
    public void announceScore(String user, int score) {
        sendChatMessage(user + " You have " + score + " Points!");
    }

    public Command parseLine(String message, String sender) {
        this.sender = sender;
        return parseLine(message);
    }

    @Override
    public Command parseLine(String message) {

        String[] parts = message.split(" ");

        // !register
        if (parts[0].equals("!register")) {
            Log.info("Register Command received");
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
            return new Ask(model, channel, question[1]);
        }

        // !answer
        if(parts[0].equals("!answer")){
            String[] qAnda = message.split("!answer ");
            qAnda = qAnda[1].split("(->)+");
            return new Answer(model,channel,qAnda[0],qAnda[1]);
        }

        // !explain
        if (parts[0].equals("!explain")) {
            String[] qAnda = message.split("!explain ");
            return new Explanation(model,channel,qAnda[1]);
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
        if (parts[0].equals("!votekick")) {
            return new Votekick(model, channel, sender);
        }

        // !streamerexplains
        if (parts[0].equals("!streamerExplains")) {
            return new StreamerExplains(model, channel, sender);
        }

        // !validate
        if (parts[0].equals("!validate")) {
            int ID = Integer.parseInt(parts[1]);
            int valScore = Integer.parseInt(parts[2]);
            return new Validate(model, channel, ID, valScore);
        }

        // !taboo
        if (parts[0].equals("!taboo")) {
            return new Taboo(model, channel, parts[1]);
        }

        // !vote
        if (parts[0].equals("!vote")) {
            int voteNum = Integer.parseInt(parts[1]);
            return new Prevote(model, channel, new int[3]);
        }


        return null;
    }
}
