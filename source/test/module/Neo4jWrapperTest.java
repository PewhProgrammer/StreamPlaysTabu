package module;

import common.DatabaseException;
import common.Log;
import common.Neo4jWrapper;
import junit.framework.TestCase;
import model.Language;
import org.junit.Test;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

import java.util.HashSet;
import java.util.Set;

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
    private final int RATING_TRESHOLD = 3 ; // How many rating to validate a connection

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
            database.createNode("nau tilus");
        }catch(DatabaseException e){
            e.getMessage();
            fail();
        }
        assertEquals("lookUp could not find the node!"
                ,true,
                database.lookUpNode("Maokai",label));

        //lookup with whitespaces
        assertEquals("lookUp could not find the node!"
                ,true,
                database.lookUpNode("Nautilus",label));

    }

    public void testCreateRelationship(){

        assertEquals("No such relationship could be created!"
                ,true,
                database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",
                        "IS RELATED TO",false));
        //test increase of rating And false flag override
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",
                "IS RELATED TO",false);
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",
                "IS RELATED TO",false);


    }

    public void testClearRelationships(){
        assertEquals("No such relationshio could be created!"
                ,true,
                database.insertNodesAndRelationshipIntoOntology("Nautilus","Nautilus",
                        "RATING",true));
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

    public void testGetTabooWords(){

        String explain = "Mario Kart";
        String relation1 = "is a character of";
        Set<String> result = new HashSet<>();
        int i = 3 ;

        database.insertNodesAndRelationshipIntoOntology("Peach",explain,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Peach",explain,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Bowser",explain,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Toad",explain,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Toad",explain,relation1,true);

        result = database.getTabooWords(explain,i); //fetch three words
        for(String r:result){
            assertFalse(r.equals("Bowser")); //Bowser only got one rating therefore is not
            //included
        }
    }

    public void testSetUpNodes(){

        try{
            database.createNode("lcs");
            database.createNode("dota 2");
            database.createNode("mp9");
            database.createNode("rush b");
            database.createNode("spellthief");
            database.createNode("infinity edge");
            database.createNode("worlds championship");
            database.createNode("lag");
            database.createNode("player versus environment");
            database.createNode("strafing");
            database.createNode("mob");
            database.createNode("spawn point");
            database.createNode("tank");
            database.createNode("train simulator");
            database.createNode("open world");
            database.createNode("buff");
            database.createNode("deathmatch");
            database.createNode("camping");
            database.createNode("avatar");
            database.createNode("hitbox");
            database.createNode("hud");
            database.createNode("hack and slash");
            database.createNode("player versus player");
            database.createNode("mario kart");
            database.createNode("mario star");
            database.createNode("geralt of rivia");
            //database.createNode("");

        }catch(DatabaseException e){
            Log.trace(e.getMessage());
            fail();
        }

    }

}
