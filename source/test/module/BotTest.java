package module;

import junit.framework.TestCase;
import logic.bots.BeamBot;
import logic.bots.Bot;
import logic.bots.TwitchBot;
import logic.commands.*;

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

    public void testAskCommand() {
        String content = "!ask Is it blue?";
        String user = "Karl";
        Ask a = new Ask(null, channel, "Is it blue?");
        String msg = getTwitchMessage(user, content, channel);
        parseInput(tb, msg, a);
    }

    public void testRegisterCommand() {
        String content = "!register";
        String user = "Karl";
        String msg = getTwitchMessage(user, content, channel);
        Register r = new Register(null, channel, user);
        parseInput(tb, msg, r);
    }

    public void testGuessCommand() {
        String content = "!guess Reis";
        String user = "Karl";
        String guess = "Reis";
        String msg = getTwitchMessage(user, content, channel);
        Guess g = new Guess(null, channel, user, guess);
        parseInput(tb, msg, g);

        content = "!guess Geralt von Riva";
        guess = "Geralt von Riva";
        msg = getTwitchMessage(user, content, channel);
        g = new Guess(null, channel, user, guess);
        parseInput(tb, msg, g);
    }

    public void testRulesCommand() {
        String user = "Karl";
        String content = "!rules";
        String msg = getTwitchMessage(user, content, channel);
        Rules r = new Rules(null, channel, user);
        parseInput(tb, msg, r);
    }

    public void testScoreCommand() {
        String user = "Karl";
        String content = "!score";
        String msg = getTwitchMessage(user, content, channel);
        Rank r = new Rank(null, channel, user);
        parseInput(tb, msg, r);
    }

    public void testVotekickCommand() {
        String user = "Karl";
        String content = "!votekick";
        String msg = getTwitchMessage(user, content, channel);
        Votekick v = new Votekick(null, channel, user);
        parseInput(tb, msg, v);
    }

    public void testStreamerexplainsCommand() {
        String user = "Karl";
        String content = "!streamerExplains";
        String msg = getTwitchMessage(user, content, channel);
        StreamerExplains se = new StreamerExplains(null, channel, user);
        parseInput(tb, msg, se);
    }

    public void testValidateCommand() {
        String user = "Karl";
        String content = "!validate 1 3";
        String msg = getTwitchMessage(user, content, channel);
        Validate v = new Validate(null, channel, 1, 3);
        parseInput(tb, msg, v);
    }

    public void testTabooCommand() {
        String suggestion = "Hufeisen";
        String user = "Karl";
        String content = "!taboo Hufeisen";
        String msg = getTwitchMessage(user, content, channel);
        Taboo t = new Taboo(null, channel, suggestion);
        parseInput(tb, msg, t);
    }

    public void testVoteCommand() {
        String user = "Karl";
        String content = "!vote 3 4 5 6 7";
        int[] id = {3, 4, 5, 6, 7};
        String msg = getTwitchMessage(user, content, channel);
        Prevote p = new Prevote(null, channel, id);
        parseInput(tb, msg, p);
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
