package module;

import common.DatabaseException;
import common.Log;
import common.Neo4jWrapper;
import common.Util;
import junit.framework.TestCase;
import model.Language;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
    private final Random randomizer = new Random(new Date().getTime());
    private static final String FILENAME = "database_config.txt";
    private final String channelName = "streamplaystaboo";

    @org.junit.Test
    public void setUp() throws Exception {
        int seed = randomizer.nextInt(100);
        database = new Neo4jWrapper(simulation,neo4jbindAddr,seed);
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
                        "IS",false));
        //test increase of rating And false flag override
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                "IS RELATED TO",false);
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                "IS RELATED TO",false);
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                "IS NOT RELATED TO",false);


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
        database.createUser(user,channelName);

        assertEquals("Points were incorrect!",
                0,database.getUserPoints(user,channelName));
        assertEquals("Points were incorrect!",
                200,database.updateUserPoints(user,200,channelName));
        assertEquals("Points were incorrect!",
                200,database.getUserPoints(user,channelName));
    }

    public void testGetUserRankings(){

        database.createUser("John","streamplaystaboo");
        LinkedHashMap<String,Integer> list = database.getHighScoreList(3,channelName);
        Log.info(list.toString());

    }

    public void testGetUserErrors(){
        String user ="Manuel";
        database.createUser(user,channelName);

        assertEquals("Points were incorrect!",
                0,database.getUserError(user,channelName));
        assertEquals("Points were incorrect!",
                1,database.increaseUserError(user,channelName));
    }

    public void testVoteKicked(){
        String user ="Manuel";
        database.createUser(user,channelName);
        database.updateUserVoteKicked("Manuel",channelName);

    }

    public void testGetExplainWord(){
        String explain = "";
        String category = "simulation";
        Set<String> usedWords = new HashSet<>();

        //test taboo word as explain word
        try {
            database.createNode("nautilus",false);
            assertEquals("Should not retrieve an explain word!","",database.getExplainWord(category,usedWords));
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }

        //test category as explain word
        try {
            database.createNode("maokai",false);
            database.setCategory("maokai");
            assertEquals("Should retrieve an explain word!","maokai",database.getExplainWord(category,usedWords));
            usedWords.add("maokai");
            database.getExplainWord(category,usedWords);
            fail();
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }

        //Test among all nodes
        try {
            database.createNode("alistar",true);
            assertEquals("Should retrieve an explain word!","alistar",database.getExplainWord(category,usedWords));
            usedWords.add("alistar");
            database.getExplainWord(category,usedWords);
            fail();
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }

    }

    public void testExplainWordRandomizer(){
        String category = "simulation";
        Set<String> usedWords = new HashSet<>();

        //MANUAL TEST
        try {
            database.createNode("nautilus",true);
            database.createNode("Maokai",true);
            database.createNode("alistar",true);
            database.createNode("Yorick",true);
            database.getExplainWord(category,usedWords);
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }
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

        Set<String> result = database.getCategories(10);
        assertTrue("given Categories are in size not equal 3" , result.size() == 3);
    }

    public void testGetTabooWords(){

        String explain = "Mario";
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

    public void testValidateExplainTaboo(){
        database.createStreamNode("streamplaystaboo");
        String explain = "league of legends";
        String taboo = "alistar";

        database.insertNodesAndRelationshipIntoOntology(taboo,explain,true,"is used for taboo",true);
        database.insertNodesAndRelationshipIntoOntology(taboo,explain,true,"has no meaning whatsoever",true);

        database.validateExplainAndTaboo(explain,taboo,2);
    }

    public void testGetUserTimeoutStamp(){
        database.createUser("John","streamplaystaboo");
        database.setUserErrorTimeStamp("John",new Date());
        String result = database.getUserErrorTimeStamp("John");

        Log.info(result);
    }

    public void testSetUpNodes(){

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {

            String sCurrentLine = br.readLine();
            sCurrentLine = br.readLine();

            while(!sCurrentLine.startsWith("CreateNodesAndRelationships:")){
                    try{
                        database.createNode(sCurrentLine,true);
                    }catch(DatabaseException e){
                        Log.trace(e.getMessage());
                        fail();
                    }
                sCurrentLine = br.readLine();
            }
            sCurrentLine = br.readLine();
            while(sCurrentLine != null){
                String[] parts = sCurrentLine.split(";");
                database.insertNodesAndRelationshipIntoOntology(parts[0],parts[2],true,parts[1],true);
                sCurrentLine = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testOntology(){

        String explainWord = "Mario";
        String[] explanation = {"The color of its suit is red","The size of its body is small"
        ,"The shape of its body is big","It wears an overall","It has a wrench","It is used for controlling",
                "It enhances gameplay","It appears in nintendo games","It is a part of a retro game","It requires a friend",
                "It has the ability to shoot fireballs","It is known for being a plumber","It is known as a hero",
                "It can be received from the beginning","It works as a player","Its character is charming","Its genre is avatar",
                "It sets in a game", "It is published by nintendo","It was released in earlier videogames day","It is typically near luigi",
                "It is typically on a ground","It is typically in the game","It is the opposite of wario","It is related to peach"};

        for(String exp:explanation){
            String[] content = Util.parseTemplate(exp);
            if(content == null) {
                Log.setLevel(Log.Level.INFO);
                Log.info("ERROR: "+ exp);
                continue;
            }
            String relation = content[0].toLowerCase();
            String targetNode = content[1].toLowerCase();
            boolean isExplain = Boolean.parseBoolean(content[2]);

            database.insertNodesAndRelationshipIntoOntology(explainWord,
                    targetNode,isExplain, relation,true);
        }


    }

}
