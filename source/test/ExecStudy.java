import common.DatabaseException;
import common.Log;
import common.Neo4jWrapper;
import junit.framework.TestCase;
import model.Language;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.Random;

/**
 * Created by Thinh-Laptop on 20.05.2017.
 */
public class ExecStudy extends TestCase {

    private boolean simulation = false;
    private String neo4jbindAddr = "pewhgames.com:7687";
    private Neo4jWrapper database;
    private final Random randomizer = new Random(new Date().getTime());
    private static final String FILENAME = "database_config.txt";
    private final String channelName = "streamplaystaboo";

    @org.junit.Test
    public void setUp() throws Exception {
        int seed = randomizer.nextInt(100);
        database = new Neo4jWrapper(simulation, neo4jbindAddr, seed);
        database.resetRelationships();
        database.resetDatabase();
        Log.setLevel(Log.Level.TRACE);
    }

    public void testSetUpNodes() {

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {

            String sCurrentLine = br.readLine();
            sCurrentLine = br.readLine();

            while (!sCurrentLine.startsWith("CreateNodesAndRelationships:")) {
                try {
                    database.createNode(sCurrentLine, true);
                } catch (DatabaseException e) {
                    Log.trace(e.getMessage());
                    fail();
                }
                sCurrentLine = br.readLine();
            }
            sCurrentLine = br.readLine();
            while (sCurrentLine != null) {
                String[] parts = sCurrentLine.split(";");
                try {
                    database.insertNodesAndRelationshipIntoOntology(parts[0], parts[2], true, parts[1], true);
                }catch (ArrayIndexOutOfBoundsException e){
                    Log.trace("Wrong Formatting: "+ parts.toString());
                }
                sCurrentLine = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
