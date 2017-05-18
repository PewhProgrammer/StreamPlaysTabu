package logic.bots;

import common.Log;
import logic.commands.*;
import model.GameModel;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.util.ArrayList;
import java.util.List;

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

    TwitchAPIRequester requester;


    private class Pirc extends PircBot {

        List<String> viewers = new ArrayList<>();
        GameModel model;

        public Pirc(String user, GameModel gm) {

            this.setName(user);
            this.setLogin("[" + user + "]");
            this.model = gm;
        }

        public void onMessage(String channel, String sender,
                              String login, String hostname, String message) {

            if (sender.equals("streamplaystaboo") | sender.equals(channel)){
                if (message.startsWith("!shutdown")) {
                    Log.debug("shutdown command received!");
                    sendMessage(channel, String.format("@" + sender + ": BYE BYE"));
                    partChannel(sender);
                    disconnect();
                    Thread.interrupted();

                }
            }

            Command cmd = parseLine(message, sender);
            if (cmd != null) {
                model.pushCommand(cmd);
            }
        }

        public void onPrivateMessage(String sender, String login, String hostname, String message) {
            Command cmd = parseLine(message, sender);

            System.out.println(message);
            String[] channel = message.split(" ");

            new AltTwitchBot(model, "#"+channel[0]);


            if (cmd != null) {
                model.pushCommand(cmd);
            }
        }





        public void onConnect(){
            System.out.println("I'm connected!");

        }

        public void onUnknown(String line){
            Log.info(line);
        }

        protected  void onAction(String sender, String login,
                                 String hostname, String target, String action){
            Log.info(action);
        }

        @Override
        protected void onUserList(String channel, User[] users){

            List<String> viewers = new ArrayList<>();

            for (int i = 0; i < users.length; i++){
                viewers.add(users[i].getNick());
                System.out.println("FOUND SOMEONE: "+users[i].getNick());
            }
        }

        protected void onJoin(String channel, String sender, String login, String hostname) {
            viewers.add(sender);
            System.out.println("HERE COMES DAT "+sender);
        }

        protected void onPart(String channel, String sender, String login, String hostname) {
            viewers.remove(sender);
            System.out.println("OH SHIT WADDUP "+sender);
        }

    }

    @Override
    public void run() {

    }

    public static boolean checkChannelExist(String ch){
        TwitchAPIRequester requester;
        requester = new TwitchAPIRequester();

        try{
            return requester.requestChannel(ch);
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void connectToChatroom(String user) {
        checkChannelExist(user);
        bot = new Pirc("streamplaystaboo", this.model);
        bot.setVerbose(true);

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
        Log.info("connected to " + user);
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
    public void whisperLink(String user, String link, int pw) {
        //sendChatMessage(" " + user + " You are the giver! Here is your link, please click it! " + link);
        sendPrivMessage("You are the giver! Here is your link: " + link + ", please click on it and use your password: " + pw + " to start explaining.", user);
        //sendPrivMessage("Your Explain word: " + model.getExplainWord(),user);

        //TODO: why onGiverJoined() at this point? think it is just to don't crash the game
        //model.getSiteBot().onGiverJoined();
        //model.getSiteController().giverJoined();
    }

    @Override
    public void announceNewRound() {
        sendChatMessage("------------------------------------------------------------------" +
                " A new round has started. Good Luck!!!" +
                " ------------------------------------------------------------------");
    }

    @Override
    public void announceWinner(String user) {
        sendChatMessage("The Winner is " + user + ". Congratulations!"); //PogChamp?
    }

    public void announceNoWinner() {
        sendChatMessage("There is no Winner!! Next time :)"); //PogChamp?
    }

    @Override
    public void announceGiverNotAccepted(String user) {
        sendChatMessage( user + " did not accept his offer to explain the word. New Registration phase!");
    }


    @Override
    public void announceRegistration() {
        sendChatMessage("" +
                "------------------------------------------------- A new round will start soon. Type !register to get into the giver pool! -------------------------------------------------");
    }

    @Override
    public void announceScore(String user, int score) {
        sendChatMessage(user + " You have " + score + " Points!");
    }

    @Override
    public List<String> getUsers(String user) {
        user = channel;

        List<String> channels = new ArrayList<>();

        User[] users = bot.getUsers(user);

        for (int i = 0; i<users.length; i++){

            channels.add(users[i].getNick());
        }

        return channels;
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
            return new Explanation(model,channel,qAnda[1], sender);
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

        // !streamerExplains
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
            int[] preVotes = new int[parts.length-1];
            for (int i = 1; i < parts.length; i++){
                int vote = Integer.parseInt(parts[i]);
                preVotes[i-1] = vote;
            }
            return new Prevote(model, channel, preVotes);
        }

        return new ChatMessage(model, channel, sender, message);
    }
}

