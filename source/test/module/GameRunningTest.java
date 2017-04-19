package module;

import common.Neo4jWrapper;
import junit.framework.TestCase;
import logic.GameControl;
import logic.bots.SiteBot;
import logic.commands.Register;
import model.GameMode;
import model.GameModel;
import model.Language;

/**
 * Created by Thinh-Laptop on 19.04.2017.
 */
public class GameRunningTest extends TestCase {

    private GameControl controller ;

    private boolean simulation = true;
    private String neo4jbindAddr = "localhost:7687";
    private final Language language = Language.Ger;
    private Neo4jWrapper database ;
    private SiteBot siteBot = new SiteBot();
    private GameModel gModel ;

    @org.junit.Test
    public void setUp() throws Exception {
        database = new Neo4jWrapper(simulation,neo4jbindAddr);
        gModel = new GameModel(language,(short)2,database,
                siteBot);
        controller = new GameControl(gModel);
    }

    public void testGameRunning(){

        gModel.getCommands().push(new Register(gModel,"","John"));
        controller.waitingForPlayers();
    }
}
