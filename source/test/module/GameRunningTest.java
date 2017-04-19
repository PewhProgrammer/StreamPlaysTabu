package module;

import common.Log;
import common.Neo4jWrapper;
import gui.GuiAnchor;
import junit.framework.TestCase;
import logic.GameControl;
import logic.bots.SiteBot;
import logic.commands.GiverJoined;
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
        Thread mTHREAD = new Thread() {
            @Override
            public void run() {
                Log.info("Launching Server...");
                new GameControl(gModel).waitingForConfig();
            }

        } ;

        mTHREAD.start();
    }

    public void testGameRunning(){

        gModel.getCommands().push(new Register(gModel,"","John"));
        //controller.waitingForPlayers();
        gModel.getCommands().push(new Register(gModel,"","Maria"));
        gModel.getCommands().push(new GiverJoined(gModel,""));

        String[] param = {"testparam"};
        if(true) {
            Log.info("Launching prototype GUI...");
            GuiAnchor anchor = new GuiAnchor();
            anchor.setModel(gModel);
            anchor.main(param);
        }
        else {
            Log.info("Launching experimental GUI...");
        }


    }
}
