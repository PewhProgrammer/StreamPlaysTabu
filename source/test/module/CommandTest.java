package module;

import common.Neo4jWrapper;
import junit.framework.TestCase;
import logic.bots.SiteBot;
import logic.commands.Answer;
import logic.commands.CategoryChosen;
import logic.commands.Command;
import model.GameMode;
import model.GameModel;
import model.Language;

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
        ,neo);
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
        Command cateChosen = new CategoryChosen(gModel,
                "",category,sBot);
        assertTrue("Category has not been chosen correctly",
                gModel.getCategory().equals(category));

    }

    public void testExplanation(){
        fail();
    }

    public void testGiverJoined(){
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
        fail();
    }

    public void testRegister(){
        fail();
    }

    public void testRules(){
        fail();
    }

    public void testSkip(){
        fail();
    }

    public void testStreamerExplains(){
        fail();
    }

    public void testTaboo(){
        fail();
    }
}
