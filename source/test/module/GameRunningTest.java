package module;

import common.Log;
import common.database.Neo4jWrapper;
import junit.framework.TestCase;
import logic.GameControl;
import model.GameModel;

/**
 * Created by Thinh-Laptop on 19.04.2017.
 */
public class GameRunningTest extends TestCase {

    private GameControl controller ;

    private boolean simulation = true;
    private String neo4jbindAddr = "localhost:7687";
    private String ext_bindAddr = "localhost:1337";
    private Neo4jWrapper database ;
    private GameModel gModel ;

    @org.junit.Test
    public void setUp() throws Exception {
        database = new Neo4jWrapper(simulation,neo4jbindAddr,20);
        gModel = new GameModel(database);
        controller = new GameControl(gModel, 1337,ext_bindAddr);
        Thread mTHREAD = new Thread() {
            @Override
            public void run() {
                Log.info("Launching Server...");
                new GameControl(gModel, 1337,ext_bindAddr).waitingForConfig();
            }

        } ;

        mTHREAD.start();
    }
}
