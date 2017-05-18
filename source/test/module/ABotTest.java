package module;

import edu.stanford.nlp.util.Beam;
import junit.framework.TestCase;
import logic.bots.AltTwitchBot;
import logic.bots.BeamBot;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by WASABI on 04.05.2017.
 */
public class ABotTest extends TestCase {

    AltTwitchBot tb;
    BeamBot bb;
    String channel = "streamplaystaboo";

    public void setUp() throws Exception{
        tb = new AltTwitchBot(null, channel);
        //bb = new BeamBot(null, channel);

    }

    public void testTwitchBot(){
        String channelToConnect = "imaqtpie";
        AltTwitchBot bot = new AltTwitchBot(null, channelToConnect);

        List<String> test = bot.getUsers(channelToConnect);

        System.out.println("Found " + test.size() + " users.");

        Iterator<String> it = test.iterator();
        while (it.hasNext()) {
            String s = it.next();
            System.out.println(s);
        }

    }

    public void testUserList() {
        List<String> users = tb.getUsers("shroud");
        Iterator<String> it = users.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

   /* public void testBeamBot(){
        BeamBot bot = null;
        try {
            bot = new BeamBot(null, "streamplaystaboo");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            System.out.println("IM HERE!!!   " + bot.getCurrentViewers("streamplaystaboo"));
    }*/


}
