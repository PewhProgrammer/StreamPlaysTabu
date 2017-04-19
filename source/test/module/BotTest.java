package module;

import junit.framework.TestCase;
import logic.bots.BeamBot;
import logic.bots.TwitchBot;

import java.util.concurrent.ExecutionException;

/**
 * Created by WASABI on 19.04.2017.
 */
public class BotTest extends TestCase{
    @org.junit.Test
    public void setUp() throws Exception {

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

}
