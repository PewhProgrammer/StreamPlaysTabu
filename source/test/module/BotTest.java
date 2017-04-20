package module;

import junit.framework.TestCase;
import logic.bots.BeamBot;
import logic.bots.Bot;
import logic.bots.TwitchBot;
import logic.commands.Ask;
import logic.commands.Command;

import java.util.concurrent.ExecutionException;

/**
 * Created by WASABI on 19.04.2017.
 */
public class BotTest extends TestCase{

    private TwitchBot tb;
    private String channel = "RealWasabiMC";

    @org.junit.Test
    public void setUp() throws Exception {
        this.tb = new TwitchBot(null, channel);
    }

    public void test(){
        try {
            BeamBot Bot = new BeamBot("k3uleeebot", null);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testAskCommand() {
        String content = "!ask Is it blue?";
        String user = "Karl";

        Ask a = new Ask(null, channel, "Is it blue?");

        String msg = getTwitchMessage(user, content, channel);
        parseInput(tb, msg, a);
    }

    private String getTwitchMessage(String user, String content, String channel) {
        return ":" + user + "!" + user + "@" + user + ".tmi.twitch.tv PRIVMSG #" + channel + " :" + content;
    }

    /* :nickname!username@nickname.tmi.twitch.tv PRIVMSG #channel :message*/
    private void parseInput(Bot b, String input, Command cmd) {
        Command parsedCmd = b.parseLine(input);
        assertEquals(cmd, parsedCmd);
    }

}
