package logic.bots;

import common.Log;
import logic.commands.*;
import model.GameModel;
import org.json.JSONArray;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.resource.chat.BeamChat;
import pro.beam.api.resource.chat.events.IncomingMessageEvent;
import pro.beam.api.resource.chat.methods.AuthenticateMessage;
import pro.beam.api.resource.chat.methods.ChatSendMethod;
import pro.beam.api.resource.chat.methods.WhisperMethod;
import pro.beam.api.resource.chat.replies.AuthenticationReply;
import pro.beam.api.resource.chat.replies.ReplyHandler;
import pro.beam.api.resource.chat.ws.BeamChatConnectable;
import pro.beam.api.services.impl.ChannelsService;
import pro.beam.api.services.impl.ChatService;
import pro.beam.api.services.impl.UsersService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class BeamBot extends Bot {

    private final BeamAPI api;
    private final BeamUser channelOwner;
    private final BeamChatConnectable chatConnectable;

    private String sender = "";


    public BeamBot(GameModel model, String user) throws ExecutionException, InterruptedException {

        super(model, user);


        BeamUser beamBot;
        BeamChannel beamChannel;
        BeamChat beamChatBot;

        /*StreamPlaysTaboo*/
        api = new BeamAPI("uy9mwJ8iWQ9O1VzPeK6M4D1akfWOpDTh69ejyeBwP5hPrItSWkY5NUSXjxGFFUtE");

        //api interface
        beamBot = api.use(UsersService.class).getCurrent().get();


        // 'user' -> id des users um uns damit channel und chat instanz zu holen
        int targetId = 0;
        try {
            targetId = getUserId(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //channel auf den wir connecten wollen
        beamChannel = api.use(ChannelsService.class).findOne(targetId).get();
        channelOwner = beamChannel.user ;

        //chat zu dem wir connecten wollen
        beamChatBot = api.use(ChatService.class).findOne(targetId).get();
        chatConnectable = beamChatBot.connectable(api);

        if (chatConnectable.connect()) {
            chatConnectable.send(AuthenticateMessage.from(beamChannel, beamBot, beamChatBot.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    chatConnectable.send(ChatSendMethod.of("Hello World! I'm StreamPlaysBot!"));
                    chatConnectable.send(WhisperMethod.builder().send("wichsa").to(channelOwner).build());
                    //announceNewRound();
                    Log.info("Succesfully connected!");
                }

                public void onFailure(Throwable var1) {
                    var1.printStackTrace();
                }

            });
        }

        run(); //starts new thread
    }

    @Override
    public void run(){

        chatConnectable.on(IncomingMessageEvent.class, event -> {

            sender = event.data.userName;
            Command cmd = parseLine(event.data.message.message.get(0).text);
            if (cmd != null) {
                model.pushCommand(cmd);
            }
        });
    }

    public Command parseLine(String message, String sender) {
        this.sender = sender;
        return parseLine(message);
    }

    @Override
    public Command parseLine(String line) {

        String[] message = line.split(" ");

            /* !register */
        if(message[0].equals("!register")) {
            return new Register(model, channel, sender);
        }

            /* !guess */
        if (message[0].equals("!guess")) {
                /* SAVE GUESS AND USERNAME*/
                String[] guess = line.split("!guess ");
            return new Guess(model, channel,sender, guess[1]);
        }

            /* !ask */
        if (message[0].equals("!ask")) {
                /*SAVE ASK SEND GUI*/
                String[] question = line.split("!ask ");
            return new Ask(model, channel, question[1], sender);
        }

            /* !rules */
        if (message[0].equals("!rules")) {
                /*SAVE NAME WHISPEr RULES*/
            return new Rules(model, channel, sender);
        }

            /* !score */
        if (message[0].equals("!score")) {
                /*CHAT RANK*/
            return new Rank(model, channel, sender);
        }

            /* !votekick */
        if ((message[0].equals("!votekick") && message.length == 1) || (message.length == 2 && message[1].equals(model.getGiver()))) {
                /* VOTEKICK DAT ASS*/
            return new Votekick(model, channel, sender);
        }

            /* !streamerexplains*/
        if (message[0].equals("!streamerExplains")) {
                /* CHECKNAME CHANGE GAMEMODE*/
            return new StreamerExplains(model, channel, sender);
        }

            /* validate */
        if (message[0].equals("!validate")) {
                /* GET ID AND SCORE */
            int ID = Integer.parseInt(message[1]);
            int valScore = Integer.parseInt(message[2]);
            return new Validate(model, channel, ID, valScore, sender);
        }

            /* !taboo */
        if (message[0].equals("!taboo")) {
                /* SAVE TABOO WORD */
            return new Taboo(model, channel, message[1], sender);
        }

            /* !vote */
        if (message[0].equals("!vote")) {
                /* GET VOTES */
            int[] preVotes = new int[message.length-1];
            for (int i = 1; i < message.length; i++){
                int vote = Integer.parseInt(message[i]);
                preVotes[i-1] = vote;
            }
            return new Prevote(model, channel,preVotes, sender);
        }
        return new ChatMessage(model, channel, sender, line);
    }

    private int getUserId(String user) throws IOException {
        String s = sendHTTPRequest("https://beam.pro/api/v1/channels/" + user + "?fields=id");
        return Integer.parseInt(s.split(":")[1].split("}")[0]);
    }

    @Override
    public void connectToChatroom(String user) {

        Log.info("Sollte niemals benutzt werden!!");
    }

    public static boolean checkChannelExists(String channel) {

        try {
            sendHTTPRequest("https://beam.pro/api/v1/channels/" + channel + "?fields=id");
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public void sendChatMessage(String msg) {
        chatConnectable.send(ChatSendMethod.of(msg));
    }

    @Override
    public void whisperRules(String user) {
        whisper(user, rules);
    }

    @Override
    public void whisperLink(String user, String link, int pw) {
        Log.trace("Send link to giver!");
        whisper(user, "You are the giver! Here is your link: " + link + ", please click on it and use your password: " + pw + " to start explaining.");
    }

    private void whisper(String receiver, String content) {

        BeamUser rec;

        try {
            int targetId = getUserId(receiver);
            rec = api.use(ChannelsService.class).findOne(targetId).get().user;
            WhisperMethod.Builder builder = WhisperMethod.builder().to(rec);
            builder.send(content);
            chatConnectable.send(builder.build());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void announceNewRound() {
        chatConnectable.send(ChatSendMethod.of("A new round has started. Good Luck and let them guesses flow!!!"));
    }

    @Override
    public void announceWinner(String user) {
        chatConnectable.send(ChatSendMethod.of(String.format("%s IS THE WINNER! CONGRATULATIONS!!!", user))); /*PogChamp?*/
    }

    @Override
    public void announceNoWinner() {
        chatConnectable.send(ChatSendMethod.of("There is no Winner!! Next time :)")); /*PogChamp?*/
    }

    @Override
    public void announceGiverNotAccepted(String user) {
        chatConnectable.send(ChatSendMethod.of(user + " did not accept his offer to explain the word. New Registration phase!"));
    }

    @Override
    public void announceRegistration() {
        sendQuestion();
        chatConnectable.send(ChatSendMethod.of("A new round will start soon. Type !register to get into the giver pool!"));
    }

    @Override
    public void announceScore(String user, int score) {
        chatConnectable.send(ChatSendMethod.of(String.format("%s. You have %d Points!", user, score)));
    }

    @Override
    public List<String> getUsers(String ch) {

        List<String> users = new LinkedList<>();

        try {
            String s = sendHTTPRequest("https://beam.pro/api/v1/chats/" + getUserId(ch) + "/users");
            JSONArray userArray = new JSONArray(s);
            for (int i = 0; i < userArray.length(); i++) {
                users.add(userArray.getJSONObject(i).getString("userName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return users;
    }

    private static String sendHTTPRequest(String url) throws IOException {
        HttpURLConnection connection;
        connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        String s;

        StringBuilder sb = new StringBuilder();
        while ((s = rd.readLine()) != null) {
            sb.append(s);
        }

        is.close();
        rd.close();

        return sb.toString();
    }

}
