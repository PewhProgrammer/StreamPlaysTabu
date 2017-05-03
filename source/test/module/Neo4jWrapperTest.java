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
            database.createNode("Nautilus",true);
            database.createNode("Nautilus",true);
            fail();
        }
        catch(ServiceUnavailableException | DatabaseException e){
            Log.trace(e.getMessage());
        }
    }

    public void testResetDatabase(){
        try {
            database.createNode("Nautilus",true);
            database.resetDatabase();
            database.createNode("Nautilus",true);
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
            database.createNode("Maokai",true);
            database.createNode("nau tilus",true);
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

        String userLabel = "userNode";
        //lookup with whitespaces
        String test = "Manuel";
        assertEquals("lookUp could not find the node " + test +"!"
                ,true,
                database.lookUpNode(test,userLabel));

    }

    public void testCreateRelationship(){

        assertEquals("No such relationship could be created!"
                ,true,
                database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                        "IS RELATED TO",false));
        //test increase of rating And false flag override
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                "IS RELATED TO",false);
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                "IS RELATED TO",false);


    }

    public void testClearRelationships(){
        assertEquals("No such relationshio could be created!"
                ,true,
                database.insertNodesAndRelationshipIntoOntology("Nautilus","Nautilus",true,
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
        String category = "simulation";
        Set<String> usedWords = new HashSet<>();
        usedWords.add("alistar");

        //Test among all nodes
        try {
            database.createNode("alistar",true);
            database.getExplainWord(category,usedWords);
            fail();
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }

        try{
            database.createNode("Hextech Gunblade",true);
            explain = database.getExplainWord(category,usedWords);
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
            fail();
        }

        //Test with right categories

    }

    public void testSetCategory(){
        //watch database for type:category

        String category = "League of Legends";
        String[] explain = {"alistar","maokai","nautilus"};
        String relation1 = "is a character from";

        try{
            database.createNode("alistar",true);
            database.createNode("maokai",true);
            database.createNode("nautilus",true);
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
        }

        database.insertNodesAndRelationshipIntoOntology(explain[0],category,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology(explain[1],category,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology(explain[2],category,true,relation1,true);
    }

    public void testReliableChange(){
        //Test if reliableflag changes with more than three entries


    }

    public void testGetCategories(){

        String[] explain = {"alistar","maokai","nautilus"};

        try{
            database.createNode(explain[0],true);
            database.createNode(explain[1],true);
            database.createNode(explain[2],true);
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
        }

        database.setCategory(explain[0]);
        database.setCategory(explain[1]);
        database.setCategory(explain[2]);

        Set<String> result = database.getCategories(3);
        assertTrue("given Categories are in size not equal 3" , result.size() == 3);
    }

    public void testGetTabooWords(){

        String explain = "Mario Kart";
        String relation1 = "It appears in ";
        Set<String> result = new HashSet<>();
        int i = 3 ;

        database.insertNodesAndRelationshipIntoOntology("Peach",explain,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Peach",explain,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Bowser",explain,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Toad",explain,true,relation1,true);
        database.insertNodesAndRelationshipIntoOntology("Toad",explain,true,relation1,true);

        result = database.getTabooWords(explain,i); //fetch three words
        for(String r:result){
            assertFalse(r.equals("Bowser")); //Bowser only got one rating therefore is not
            //included
        }
    }

    public void testSetUpNodes(){

        String explain = "Mario Kart";
        String[] relation =
                {"none","is a character of"};

        try{
            database.createNode("lcs",true);
            database.createNode("dota 2",true);
            database.createNode("mp9",true);
            database.createNode("rush b",true);
            database.createNode("spellthief",true);
            database.createNode("infinity edge",true);
            database.createNode("worlds championship",true);
            database.createNode("lag",true);
            database.createNode("player versus environment",true);
            database.createNode("strafing",true);
            database.createNode("mob",true);
            database.createNode("spawn point",true);
            database.createNode("tank",true);
            database.createNode("train simulator",true);
            database.createNode("open world",true);
            database.createNode("buff",true);
            database.createNode("deathmatch",true);
            database.createNode("camping",true);
            database.createNode("avatar",true);
            database.createNode("hitbox",true);
            database.createNode("hud",true);
            database.createNode("hack and slash",true);
            database.createNode("player versus player",true);
            database.createNode("mario star",true);
            database.createNode("geralt of rivia",true);
            //database.createNode("");

        }catch(DatabaseException e){
            Log.trace(e.getMessage());
            fail();
        }

        database.insertNodesAndRelationshipIntoOntology("Mini Bowser",explain,true,relation[1],true);
        database.insertNodesAndRelationshipIntoOntology("Peach",explain,true,relation[1],true);
        database.insertNodesAndRelationshipIntoOntology("Bowser",explain,true,relation[1],true);
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,true,relation[1],true);
        database.insertNodesAndRelationshipIntoOntology("Mario",explain,true,relation[1],true);
        database.insertNodesAndRelationshipIntoOntology("Wario",explain,true,relation[1],true);
        database.insertNodesAndRelationshipIntoOntology("Daisy",explain,true,relation[1],true);
        database.insertNodesAndRelationshipIntoOntology("Toad",explain,true,relation[1],true);



    }

}
