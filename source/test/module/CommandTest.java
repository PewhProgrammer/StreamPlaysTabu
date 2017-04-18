package module;

import common.Neo4jWrapper;
import junit.framework.TestCase;
import logic.bots.SiteBot;
import logic.commands.*;
import model.GameMode;
import model.GameModel;
import model.GameState;
import model.Language;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Thinh-Laptop on 17.04.2017.
 */
public class CommandTest extends TestCase{

    private GameModel gModel;
    private final short MIN_PLAYERS =2;
    private final Language lang = Language.Ger;
    private final Neo4jWrapper neo =
            new Neo4jWrapper(true,
                    "localhost:7687");
    private final SiteBot sBot = new SiteBot();

    @org.junit.Test
    public void setUp() throws Exception {
        gModel = new GameModel(lang,MIN_PLAYERS
        ,neo, new SiteBot());
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
        String dummy = "negatve";
        Command expCommand = new Explanation(gModel,"",explanation);
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

        Command regCommand = new Register(gModel,"");
        gModel.setGameState(GameState.GameClosed);
        assertFalse("Validation passed @ " +
                getName(), regCommand.validate());
        gModel.setGameState(GameState.Registration);
        assertTrue("Validation failed @ " +
                getName(), regCommand.validate());
    }

    public void testRules(){}

    public void testSkip(){
        String giver = "John";
        int currentScore = neo.getUserPoints(giver);
        Command giverJoinedCommand = new GiverJoined(gModel,"",giver);
        Command skipCommand = new Skip(gModel,"");

        skipCommand.validate();
        skipCommand.execute();

        int updatedScore = neo.getUserPoints(giver);
        assertNotSame("User's point did not decrease",
                currentScore,updatedScore);
    }

    public void testStreamerExplains(){
        Command streamExplainsCommand = new StreamerExplains(gModel,"");

        gModel.setGameMode(GameMode.Normal);
        streamExplainsCommand.validate(); //komisch zu überprüfen
        streamExplainsCommand.execute();

        assertTrue("Game Mode has not been updated.",gModel.getGameMode() == GameMode.Streamer);
    }

    public void testTaboo(){
        String giver = "John";
        Command tabooCommand = new Taboo(gModel,"");
        Command giverJoinedCommand = new GiverJoined(gModel,"",giver);

        giverJoinedCommand.validate();
        giverJoinedCommand.execute();

        Set<String> taboos1 = gModel.getTabooWords();

        tabooCommand.validate();
        tabooCommand.execute();

        assertFalse("Taboo list didnt update!",
                gModel.getTabooWords().equals(taboos1));




    }
}
