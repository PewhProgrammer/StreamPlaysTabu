package module;


import junit.framework.TestCase;
import logic.bots.AltTwitchBot;

import java.util.Iterator;
import java.util.List;

public class ABotTest extends TestCase {

    AltTwitchBot tb;
    String channel = "streamplaystaboo";

    public void setUp() throws Exception{
        tb = new AltTwitchBot(null, channel);
    }

    public void testTwitchBot(){
        String channelToConnect = "streamplaystaboo";
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
}
