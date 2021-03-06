import common.database.DatabaseException;
import common.Log;
import common.database.Neo4jWrapper;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * Created by Thinh-Laptop on 20.05.2017.
 */
public class ExecStudy extends TestCase {

    private boolean simulation = false;
    private String neo4jbindAddr = "pewhgames.com:7687";
    //private String neo4jbindAddr = "localhost:7687";
    private Neo4jWrapper database;
    private final Random randomizer = new Random(new Date().getTime());
    private static final String FILENAME = "database_config.txt";
    private final String channelName = "streamplaystaboo";

    @org.junit.Test
    public void setUp() throws Exception {
        int seed = randomizer.nextInt(100);
        database = new Neo4jWrapper(simulation, neo4jbindAddr, seed,"neo4j","streamplaystabu");
        //database.resetRelationships();
        //database.resetDatabase();
        Log.setLevel(Log.Level.TRACE);
    }

    public void testGetExplainWord(){
        String category = "league of legends";
        String explain = "";
        try {
            explain = database.getExplainWord(category, new HashSet<>());
        }catch(DatabaseException e){

        }
        System.out.print(explain);

    }

    public void testSetUpNodes() {
        database.resetRelationships();
        database.resetDatabase();
        database.initLogging();
        database.triggerPreset();
        database.setSimulation(false);

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {

            String sCurrentLine = br.readLine();
            sCurrentLine = br.readLine();

            int i = 0 ;
            while (!sCurrentLine.startsWith("CreateNodesAndRelationships:")) {
                try {
                    database.createNode(sCurrentLine, true);
                    i++;
                    if(i == 15) database.setSimulation(true);
                } catch (DatabaseException e) {
                    Log.db(e.getMessage());
                    //fail();
                }
                sCurrentLine = br.readLine();
            }
            i = 0;
            sCurrentLine = br.readLine();
            database.setSimulation(false);
            while (sCurrentLine != null) {
                String[] parts = sCurrentLine.split(";");
                try {
                    i++;
                    if(i == 16) database.setSimulation(true);
                    database.insertNodesAndRelationshipIntoOntology(parts[0], parts[2], true, parts[1], true,"");
                }catch (ArrayIndexOutOfBoundsException e){
                    Log.db("Wrong Formatting: "+ parts.toString());
                }
                sCurrentLine = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testGetUserRankings(){
        LinkedHashMap<String,Integer> list = database.getHighScoreList(10,channelName);
        Log.info(list.toString());
    }
}
