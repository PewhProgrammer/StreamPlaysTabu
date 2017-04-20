package logic.bots;

import common.Log;
import logic.commands.Command;
import org.jibble.pircbot.PircBot;

/**
 * Created by Thinh-Laptop on 20.04.2017.
 */
public class AltTwitchBot extends Bot {

    private Pirc bot;

    public AltTwitchBot(){
        connectToChatroom("pewhtv");
    }

    private class Pirc extends PircBot{
        public Pirc(String user){
            this.setName(user);
            this.setLogin("["+user+"]");
        }

        public void onMessage(String channel, String sender,
                              String login, String hostname, String message) {
            //sendMessage(channel,"@" + sender + " " +curse.get(r.nextInt(curse.size())));

            Log.info("Received");
            sendMessage(channel,"halts maul");
            if (message.equalsIgnoreCase("thinh")) {
                sendMessage(channel, sender + ": just wrote my name huiiii");
            }

            System.out.println(sender.toString());
            if(sender.equals("hci_livestreaming") && message.startsWith("!LivestreamingMeetsHCI")){
                Log.debug("Correct sender " + sender.toString());
                StringBuffer target = new StringBuffer(message);
                target.replace( 0 ,23 ,"");
                String[] Tokens = target.toString().split(";");
                //chatConnectable.send(ChatSendMethod.of(String.format("Ping! %d tokens detected from %s",Tokens.length,event.data.userName)));
                for(String s:Tokens){
                    if(s.equals("NexXw5")) {
                        //sendMessage(channel,": Ping! I found my token!!");
                        continue;
                    }
                }
            }

            if(sender.equals("pewhtv")){
                if(message.startsWith("!print")) {
                    Log.debug("Print command received!");
                    sendMessage(channel, String.format("@" + sender + ": Ping! I've counted %d occurences on twitch! Kappa"));
                }
                if(message.startsWith("!shutdown")){
                    Log.debug("Shutdown command received!");
                    sendMessage(channel, "Ping! I'm going offline!!");
                    System.exit(1);
                }
            }
        }
    }
    @Override
    public void run() {

    }

    @Override
    public void connectToChatroom(String user) {
        bot = new Pirc("pewhtv");

        // Connect to the IRC server.
        try {
            bot.connect("irc.chat.twitch.tv",6667,"oauth:"+ "a7cc5zbe6gc7uj5uzz8p97zjkijy54");
        }catch(Exception e){
            Log.trace("HTTPResponse bot connection failure");
            System.exit(1);
        }

        bot.joinChannel("#realwasabimc");
        Log.info("connected");
    }

    @Override
    public void disconnectFromChatroom(String user) {

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
    public void announceWinner(String user) {

    }

    @Override
    public void announceRegistration() {

    }

    @Override
    public void announceScore(String user, int score) {

    }

    @Override
    public Command parseLine(String line) {
        return null;
    }
}
