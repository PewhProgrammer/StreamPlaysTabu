package logic.bots;

import common.Log;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.chat.BeamChat;
import pro.beam.api.resource.chat.events.IncomingMessageEvent;
import pro.beam.api.resource.chat.methods.AuthenticateMessage;
import pro.beam.api.resource.chat.methods.ChatSendMethod;
import pro.beam.api.resource.chat.replies.AuthenticationReply;
import pro.beam.api.resource.chat.replies.ReplyHandler;
import pro.beam.api.resource.chat.ws.BeamChatConnectable;
import pro.beam.api.services.impl.ChatService;
import pro.beam.api.services.impl.UsersService;

import java.util.concurrent.ExecutionException;

/**
 * Created by Marc on 03.04.2017.
 */
public class BeamBot extends Bot {

    private final BeamAPI api;
    private final BeamUser beamBot;
    private final BeamUser beamChannelUser;
    private final BeamChat beamChatBot;
    private final BeamChatConnectable chatConnectable;

    /**
     * Jede Bot Instanz kann nur einmal zum Chat connecten, da chatConnectable final sein muss und
     * nicht überschrieben werden kann
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public BeamBot(String user) throws ExecutionException, InterruptedException{
        /*TODO Change token to our Bot*/
        api = new BeamAPI("IlIBO49aTNglsEkhvhLwTsUbHW8j7gKZXtEE8sCQC0boEkjg2CSaLTHUByVDrqFo");

        beamBot = api.use(UsersService.class).getCurrent().get();
        beamChatBot = api.use(ChatService.class).findOne(657439).get();

        beamChannelUser = api.use(UsersService.class).search(user).get().get(0);
        chatConnectable= beamChatBot.connectable(api);

        if (chatConnectable.connect()) {
            chatConnectable.send(AuthenticateMessage.from(beamChannelUser.channel, beamBot, beamChatBot.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    chatConnectable.send(ChatSendMethod.of("Hello World! I'm StreamPlaysBot!"));
                    Log.info("Succesfully connected!");
                }
                public void onFailure(Throwable var1) {
                    var1.printStackTrace();
                }
            });
        }

        chatConnectable.on(IncomingMessageEvent.class, event -> {

            /* DO STUFF */

        });
    }

    @Override
    public void connectToChatroom(String user) {

        Log.info("Sollte niemals benutzt werden!!");
    }

    @Override
    public void sendChatMessage(String msg) {
        chatConnectable.send(ChatSendMethod.of(String.format(msg)));
    }

    @Override
    public void whisperRules(String user) {
        chatConnectable.send(ChatSendMethod.of(String.format(rules))); // TODO whispern
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
