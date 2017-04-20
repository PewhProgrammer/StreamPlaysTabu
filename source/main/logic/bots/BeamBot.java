package logic.bots;

import common.Log;
import logic.commands.*;
import model.GameModel;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.resource.chat.BeamChat;
import pro.beam.api.resource.chat.events.IncomingMessageEvent;
import pro.beam.api.resource.chat.methods.AuthenticateMessage;
import pro.beam.api.resource.chat.methods.ChatSendMethod;
import pro.beam.api.resource.chat.replies.AuthenticationReply;
import pro.beam.api.resource.chat.replies.ReplyHandler;
import pro.beam.api.resource.chat.ws.BeamChatConnectable;
import pro.beam.api.services.impl.ChannelsService;
import pro.beam.api.services.impl.ChatService;
import pro.beam.api.services.impl.UsersService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Marc on 03.04.2017.
 */
public class BeamBot extends Bot {

    private final BeamAPI api;
    private final BeamUser beamBot;
    private final BeamChannel beamChannel;
    private final BeamChat beamChatBot;
    private final BeamChatConnectable chatConnectable;

    private String sender = "";

    /**
     * Jede Bot Instanz kann nur einmal zum Chat connecten, da chatConnectable final sein muss und
     * nicht überschrieben werden kann
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public BeamBot(String user, GameModel model) throws ExecutionException, InterruptedException {
        channel = user;
        this.model = model;

        /*TODO Change token to our Bot*/
        api = new BeamAPI("IlIBO49aTNglsEkhvhLwTsUbHW8j7gKZXtEE8sCQC0boEkjg2CSaLTHUByVDrqFo");

        //api interface
        beamBot = api.use(UsersService.class).getCurrent().get();

        // 'user' -> id des users um uns damit channel und chat instanz zu holen

        //ID von user zu dem wir connecten wollen
        //TODO find target ID
        int targetId = 0;
        try {
            targetId = getUserId(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //channel auf den wir connecten wollen
        beamChannel = api.use(ChannelsService.class).findOne(targetId).get();
        //chat zu dem wir connecten wollen
        beamChatBot = api.use(ChatService.class).findOne(targetId).get();
        chatConnectable = beamChatBot.connectable(api);

        if (chatConnectable.connect()) {
            chatConnectable.send(AuthenticateMessage.from(beamChannel, beamBot, beamChatBot.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    chatConnectable.send(ChatSendMethod.of("Hello World! I'm StreamPlaysBot!"));
                    Log.info("Succesfully connected!");
                }

                public void onFailure(Throwable var1) {
                    var1.printStackTrace();
                }
            });
        }
    }

    @Override
    public void run(){

        chatConnectable.on(IncomingMessageEvent.class, event -> {
            sender = event.data.userName;
            Command cmd = parseLine(event.data.message.message.get(0).text, sender);
            if (cmd != null) {
                model.pushCommand(cmd);
            }

        });
    }

    @Override
    public Command parseLine(String line, String sender) {

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
            return new Ask(model, channel, question[1]);
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
        if (message[0].equals("!votekick")) {
                /* VOTEKICK DAT ASS*/
            return new Votekick(model, channel, sender);

        }

            /* !streamerexplains*/
        if (message[0].equals("!streamerexplains")) {
                /* CHECKNAME CHANGE GAMEMODE*/
            return new StreamerExplains(model, channel, sender);
        }

            /* validate */
        if (message[0].equals("!validate")) {
                /* GET ID AND SCORE */
            int ID = Integer.parseInt(message[1]);
            int valScore = Integer.parseInt(message[2]);
            return new Validate(model, channel, ID, valScore);
        }

            /* !taboo */
        if (message[0].equals("!taboo")) {
                /* SAVE TABOO WORD */
            return new Taboo(model, channel, message[1]);
        }

            /* !vote */
        if (message[0].equals("!vote")) {
                /* GET VOTES */
            int voteNum = Integer.parseInt(message[1]);
            return new Prevote(model, channel, new int[3]);
        }
        return null;

    }

    private int getUserId(String user) throws MalformedURLException,IOException {
        HttpURLConnection connection;
        connection = (HttpURLConnection) new URL("https://beam.pro/api/v1/channels/" + user + "?fields=id").openConnection();
        connection.setRequestMethod("GET");

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        int id = Integer.parseInt(rd.readLine().split(":")[1].split("}")[0]);

        is.close();
        rd.close();

        return id;



    }

    @Override
    public void connectToChatroom(String user) {

        Log.info("Sollte niemals benutzt werden!!");
}

    @Override
    public void disconnectFromChatroom(String user) {

    }

    @Override
    public void sendChatMessage(String msg) {
        chatConnectable.send(ChatSendMethod.of(String.format(msg)));
    }

    @Override
    public void whisperRules(String user) {
        chatConnectable.send(ChatSendMethod.of(String.format("/whisper %s Rules Rules Rules Rules", user)));
    }

    @Override
    public void whisperLink(String user, String link) {
        chatConnectable.send(ChatSendMethod.of(String.format("/whisper %s You are the giver! Here is your link, please click it!: %s", user, link)));
    }

    @Override
    public void announceNewRound() {
        chatConnectable.send(ChatSendMethod.of(String.format("A new round has started. Good Luck and let them guesses flow!!!")));
    }

    @Override
    public void announceWinner(String user) {
        chatConnectable.send(ChatSendMethod.of(String.format("%s IS THE WINNER! CONGRATULATIONS!!!", user))); /*PogChamp?*/
    }

    @Override
    public void announceRegistration() {
        chatConnectable.send(ChatSendMethod.of(String.format("A new round will start soon. Type !register to get into the giver pool!")));
    }

    @Override
    public void announceScore(String user, int score) {
        chatConnectable.send(ChatSendMethod.of(String.format("%s. You have %d Points!", user, score)));
    }



}
