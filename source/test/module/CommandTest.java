package module;

import common.Log;
import common.database.Neo4jWrapper;
import junit.framework.TestCase;
import logic.commands.*;
import model.GameMode;
import model.GameModel;
import model.GameState;
import model.Language;

import java.util.*;

/**
 * Created by Thinh-Laptop on 17.04.2017.
 */
public class CommandTest extends TestCase{

    private GameModel gModel;
    private final Neo4jWrapper neo =
            new Neo4jWrapper(true,
                    "localhost:7687",20);
    private final String channelName = "streamplaystaboo";

    @org.junit.Test
    public void setUp() throws Exception {
        gModel = new GameModel(neo);
    }

    public void testCode(){
        game:
        while(true){
            Log.info("was geht");
            break game;
        }
    }

    public void testAnswer(){
        String q = "How am i?";
        String a = "I am good";
        Command qAnda = new Answer(gModel,"",q,a);
        qAnda.validate(); qAnda.execute();

        gModel.getQAndA().forEach(t -> {
            if(t[0].equals(q) && t[1].equals(a)) return;
        });

        fail("Question-Answer pair not found");
    }

    public void testAsk(){
        //HOW TO TEST
        return;
    }

    public void testCategoryChosen(){
        String category = "League of Legends";
        Command cateChosenCommand = new CategoryChosen(gModel,
                "",category);
        assertTrue("Validation failed @ " +
                getName(), cateChosenCommand.validate());
        cateChosenCommand.execute();
        assertTrue("Category has not been chosen correctly",
                gModel.getCategory().equals(category));

    }

    public void testExplanation(){
        String explanation = "It is used for killing mobs.";
        String dummy = "negative";
        Command expCommand = new Explanation(gModel,"",explanation,"John");
        gModel.setGameState(GameState.WaitingForGiver);
        assertFalse("Validation passed @ " +
                getName(), expCommand.validate());
        gModel.setGameState(GameState.GameStarted);
        assertTrue("Validation failed @ " +
                getName(), expCommand.validate());
        expCommand.execute();
        assertTrue("Could not find explanation in game model!",gModel.getExplanations().stream().
                filter(p -> p.equals(explanation)).findAny().isPresent());
        assertFalse("Found explanation even though should not have!",gModel.getExplanations().stream().
                filter(p -> p.equals(dummy)).findAny().isPresent());
    }

    public void testGiverJoined()
    {
        fail();
    }

    public void testGiverLeft(){
        fail();
    }

    public void testGuess(){
        fail();
    }

    public void testHost(){
        fail();
    }

    public void testRank(){
    }

    public void testRegister(){

        Command regCommand = new Register(gModel,"", "");
        gModel.setGameState(GameState.GameStarted);
        assertFalse("Validation passed @ " +
                getName(), regCommand.validate());
        gModel.setGameState(GameState.Registration);
        assertTrue("Validation failed @ " +
                getName(), regCommand.validate());
    }

    public void testRules(){}

    public void testSkip(){
        String giver = "John";
        int currentScore = neo.getUserPoints(giver,channelName);
        Command giverJoinedCommand = new GiverJoined(gModel,"");
        Command skipCommand = new Skip(gModel,"");

        skipCommand.validate();
        skipCommand.execute();

        int updatedScore = neo.getUserPoints(giver,channelName);
        assertNotSame("User's point did not decrease",
                currentScore,updatedScore);
    }

    public void testStreamerExplains(){
        Command streamExplainsCommand = new StreamerExplains(gModel,"", "");

        gModel.setGameMode(GameMode.Normal);
        streamExplainsCommand.validate(); //komisch zu überprüfen
        streamExplainsCommand.execute();

        assertTrue("Game Mode has not been updated.",gModel.getGameMode() == GameMode.Streamer);
    }

    public void testTaboo(){
        String giver = "John";
        Command tabooCommand = new Taboo(gModel,"", "", "Test");
        Command giverJoinedCommand = new GiverJoined(gModel,"");

        giverJoinedCommand.validate();
        giverJoinedCommand.execute();

        Set<String> taboos1 = gModel.getTabooWords();

        tabooCommand.validate();
        tabooCommand.execute();

        assertFalse("Taboo list didnt update!",
                gModel.getTabooWords().equals(taboos1));




    }

    public void testValidation() {

        gModel.setGameState(GameState.Registration);

        Set<String> tabooWords = new HashSet<>();
        String[] words = {"dummy", "taboo", "words", "for", "validation"};
        ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));
        tabooWords.addAll(wordList);

        gModel.setTabooWords(tabooWords);

        //public Validate(GameModel gm, String ch, String word, int valScore, String sender) {
        Validate errorCmd = new Validate(gModel, "streamplaystaboo", "haha", 3, "pigfacejoe2");
        assertFalse(errorCmd.validate());

        Validate correctCmd = new Validate(gModel, "streamplaystaboo", "words", 3, "pigfacejoe2");
        assertTrue(correctCmd.validate());

    }
}
