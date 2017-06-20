package module;

import junit.framework.TestCase;
import logic.bots.AltTwitchBot;
import logic.commands.*;


/**
 * Created by WASABI on 19.04.2017.
 */
public class BotTest extends TestCase{

    private AltTwitchBot atb;
    private String channel = "streamplaystaboo";
    private String user = "Karl";

    @org.junit.Test
    public void setUp() throws Exception {
        atb = new AltTwitchBot(null, channel);
    }

    public void testAskCommand() {
        String content = "!ask Is it blue?";
        Ask a = new Ask(null, channel, "Is it blue?", "Test");
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
        Validate v = new Validate(null, channel, 1, 3,"Test");
        parseInput(content, v);
    }

    public void testTabooCommand() {
        String suggestion = "Hufeisen";
        String content = "!taboo Hufeisen";
        Taboo t = new Taboo(null, channel, suggestion,"Test");
        parseInput(content, t);
    }

    public void testVoteCommand() {
        String content = "!vote 3 4 5 6";
        int[] id = {3, 4, 5, 6};
        Prevote p = new Prevote(null, channel, id, "Test");
        parseInput(content, p);
    }

    private String getTwitchMessage(String content) {
        return ":" + user + "!" + user + "@" + user + ".tmi.twitch.tv PRIVMSG #" + channel + " :" + content;
    }

    /* :nickname!username@nickname.tmi.twitch.tv PRIVMSG #channel :message*/
    private void parseInput(String content, Command cmd) {
      //  Command parsedCmd = atb.parseLine(content, user);
      //  assertEquals(cmd, parsedCmd);
        //Command parsedCmd = atb.parseLine(content, user);
        //assertEquals(cmd, parsedCmd);
    }
}
