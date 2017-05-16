package module;

import junit.framework.TestCase;
import logic.bots.TwitchAPIRequester;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by WASABI on 15.05.2017.
 */
public class RequestTest extends TestCase {

    TwitchAPIRequester request;

    @Test
    public void setUp() {
        request = new TwitchAPIRequester();
    }

    public void testValidChannels() {
        String[] channels = {"RealWasabiMC", "k3uleee", "igotaBot", "streamplaystaboo"};
        request(channels, true);
    }

    public void testInvalidChannels() {
        String[] channels = {"RealWas", "k3uleeeeeeeeeeeeeeeeeee", "igo0otaBot", "hehehehehehehlolxd"};
        request(channels, false);
    }

    private void request(String[] channels, boolean b) {
        for (String s : channels) {
            try {
                assertEquals("Channel " + s,request.requestChannel(s), b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
