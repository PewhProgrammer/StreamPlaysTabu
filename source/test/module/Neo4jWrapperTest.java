package module;

import common.DatabaseException;
import common.Log;
import common.Neo4jWrapper;
import junit.framework.TestCase;
import model.Language;
import org.junit.Test;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

/**
 * Created by Thinh-Laptop on 16.04.2017.
 */
public class Neo4jWrapperTest extends TestCase {

    private final short players = 2 ;
    private boolean simulation = true;
    private String neo4jbindAddr = "localhost:7687";
    private final Language language = Language.Ger;
    private Neo4jWrapper database ;
    private final String label = "Node";

    @org.junit.Test
    public void setUp() throws Exception {
        database = new Neo4jWrapper(simulation,neo4jbindAddr,20);
        database.resetRelationships(); //TODO doesnt work properly on userNode
        database.resetDatabase();
        Log.setLevel(Log.Level.TRACE);
    }

    public void testDoReset(){

    }

    public void testCreateNode(){
        try {
            database.createNode("Nautilus");
            database.createNode("Nautilus");
            fail();
        }
        catch(ServiceUnavailableException | DatabaseException e){
            Log.trace(e.getMessage());
        }
    }

    public void testResetDatabase(){
        try {
            database.createNode("Nautilus");
            database.resetDatabase();
            database.createNode("Nautilus");
        }
        catch(ServiceUnavailableException | DatabaseException e){
            Log.info(e.getMessage());
            fail();
        }
    }

    public void testLookUpNode(){
        assertEquals("Should not find the Node!"
                ,false,
                database.lookUpNode("Maokai",label));
        try {
            database.createNode("Maokai");
        }catch(DatabaseException e){
            e.getMessage();
            fail();
        }
        assertEquals("lookUp could not find the node!"
                ,true,
                database.lookUpNode("Maokai",label));
    }

    public void testCreateRelationship(){
        try {
            database.createNode("Nautilus");
            database.createNode("Hallo");
        }
        catch(ServiceUnavailableException | DatabaseException e){
            Log.info(e.getMessage());
        }
        assertEquals("No such relationshio could be created!"
                ,true,
                database.createRelationship("Nautilus","Hallo",
                        "RATING"));
        database.createRelationship("Nautilus","Hallo",
                "RATING");
        database.createRelationship("Nautilus","Hallo",
                "RATING");

    }

    public void testClearRelationships(){
        assertEquals("No such relationshio could be created!"
                ,true,
                database.createRelationship("Nautilus","Nautilus",
                        "RATING"));
        assertEquals("Something went wrong when clearing the ratings!"
                ,true,
                database.clearFailedRelationships());
    }

    public void testGetUserPoints(){
        String user ="John";
        database.createUser(user);

        assertEquals("Points were incorrect!",
                0,database.getUserPoints(user));
        assertEquals("Points were incorrect!",
                200,database.updateUserPoints(user,200));
        assertEquals("Points were incorrect!",
                200,database.getUserPoints(user));
    }

    public void testGetUserErrors(){
        String user ="Manuel";
        database.createUser(user);

        assertEquals("Points were incorrect!",
                0,database.getUserError(user));
        assertEquals("Points were incorrect!",
                1,database.increaseUserError(user));
    }

    public void testGetExplainWord(){
        String user ="Manuel";
        database.createUser(user);

        String explain = "";

        try {
            database.getExplainWord("simulation",null);
            fail();
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }

        try{
            database.createNode("Hextech Gunblade");
            explain = database.getExplainWord("simulation",null);
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
            fail();
        }

    }

    public void testSetUpNodes(){
        String user ="Manuel";
        database.createUser(user);

        try{
            database.createNode("LCS");
            database.createNode("Dota 2");
            database.createNode("MP9");
            database.createNode("Rush B");
            database.createNode("Spellthief");
            database.createNode("Infinity Edge");
            database.createNode("Worlds Championship");
            database.createNode("Lag");
            database.createNode("Player versus Environment");
            database.createNode("Strafing");
            database.createNode("Mob");
            database.createNode("Spawn point");
            database.createNode("Tank");
            database.createNode("Train Simulator");
            database.createNode("Open World");
            database.createNode("Buff");
            database.createNode("Deathmatch");
            database.createNode("Camping");
            database.createNode("Avatar");
            database.createNode("Hitbox");
            database.createNode("HUD");
            database.createNode("Hack and Slash");
            database.createNode("Player versus Player");

        }catch(DatabaseException e){
            Log.trace(e.getMessage());
            fail();
        }

    }

}
