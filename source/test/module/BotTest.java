package module;

import junit.framework.TestCase;
import logic.bots.AltTwitchBot;
import logic.bots.BeamBot;
import logic.commands.*;

import java.util.concurrent.ExecutionException;

/**
 * Created by WASABI on 19.04.2017.
 */
public class BotTest extends TestCase{

    private BeamBot bb;
    private AltTwitchBot atb;
    private String channel = "streamplaystaboo";
    private String user = "Karl";

    @org.junit.Test
    public void setUp() throws Exception {
        bb = new BeamBot(null, channel);
        atb = new AltTwitchBot(null, channel);
    }

    public void test(){
        BeamBot bot = null;
        try {
            bot = new BeamBot(null, "k3uleeebot");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testAskCommand() {
        String content = "!ask Is it blue?";
        Ask a = new Ask(null, channel, "Is it blue?");
        parseInput(content, a);
    }

    public void testRegisterCommand() {
        String content = "!register";
        Register r = new Register(null, channel, user);
        parseInput(content, r);
    }

    public void testGuessCommand() {
        String content = "!guess Reis";
        String guess = "Reis";
        Guess g = new Guess(null, channel, user, guess);
        parseInput(content, g);

        content = "!guess Geralt von Riva";
        guess = "Geralt von Riva";
        g = new Guess(null, channel, user, guess);
        parseInput(content, g);
    }

    public void testRulesCommand() {
        String content = "!rules";
        Rules r = new Rules(null, channel, user);
        parseInput(content, r);
    }

    public void testScoreCommand() {
        String content = "!score";
        Rank r = new Rank(null, channel, user);
        parseInput(content, r);
    }

    public void testVotekickCommand() {
        String content = "!votekick";
        Votekick v = new Votekick(null, channel, user);
        parseInput(content, v);
    }

    public void testStreamerexplainsCommand() {
        String content = "!streamerExplains";
        StreamerExplains se = new StreamerExplains(null, channel, user);
        parseInput(content, se);
    }

    public void testValidateCommand() {
        String content = "!validate 1 3";
        Validate v = new Validate(null, channel, 1, 3);
        parseInput(content, v);
    }

    public void testTabooCommand() {
        String suggestion = "Hufeisen";
        String content = "!taboo Hufeisen";
        Taboo t = new Taboo(null, channel, suggestion);
        parseInput(content, t);
    }

    public void testVoteCommand() {
        String content = "!vote 3 4 5 6";
        int[] id = {3, 4, 5, 6};
        Prevote p = new Prevote(null, channel, id);
        parseInput(content, p);
    }

    private String getTwitchMessage(String content) {
        return ":" + user + "!" + user + "@" + user + ".tmi.twitch.tv PRIVMSG #" + channel + " :" + content;
    }

    /* :nickname!username@nickname.tmi.twitch.tv PRIVMSG #channel :message*/
    private void parseInput(String content, Command cmd) {
        Command parsedCmd = bb.parseLine(content, user);
        assertEquals(cmd, parsedCmd);
        //Command parsedCmd = atb.parseLine(content, user);
        //assertEquals(cmd, parsedCmd);
    }
}
