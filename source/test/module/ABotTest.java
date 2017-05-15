package module;

import junit.framework.TestCase;
import logic.bots.AltTwitchBot;
import logic.bots.BeamBot;

import java.util.concurrent.ExecutionException;

/**
 * Created by WASABI on 04.05.2017.
 */
public class ABotTest extends TestCase {

    AltTwitchBot tb;
    String channel = "streamplaystaboo";

    public void setUp() throws Exception{
        tb = new AltTwitchBot(null, channel);
    }

    public void test(){
        AltTwitchBot bot = null;
        bot = new AltTwitchBot(null, "streamplaystaboo");

        bot.sendChatMessage("Hello!");
    }


}
