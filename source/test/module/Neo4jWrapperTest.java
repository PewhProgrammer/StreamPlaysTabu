package module;

import common.database.DatabaseException;
import common.Log;
import common.database.Neo4jWrapper;
import common.Util;
import gui.webinterface.containers.StreamRankingContainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Thinh-Laptop on 16.04.2017.
 */
public class Neo4jWrapperTest {

    private Neo4jWrapper database ;
    private final int RATING_TRESHOLD = 3 ; // How many rating to validate a connection
    private final Random randomizer = new Random(new Date().getTime());
    private static final String FILENAME = "database_config.txt";
    private final String channelName = "streamplaystaboo";

    @Before
    @org.junit.Test
    public void setUp() throws Exception {
        int seed = randomizer.nextInt(100);
        database = new Neo4jWrapper(true, "pewhgames.com:7687",seed);
        //database.resetRelationships();
        //database.resetDatabase();
        Log.setLevel(Log.Level.INFO);
    }

    @Test
    public void testDoReset(){

    }

    @Test
    public void testCreateNode(){
        try {
            database.createNode("Nautilus",true);
            database.createNode("Nautilus",true);
            Assert.fail();
        }
        catch(ServiceUnavailableException | DatabaseException e){
            Log.db(e.getMessage());
        }
    }


    @Test
    public void createMultipleUser(){
        String[] usr = {"John","John","Berta","Joseph","Genital"};
        String ch = "streamplaystaboo";
        try {
            database.createUser(usr[0],ch);
            database.createUser(usr[1],ch);
            database.createUser(usr[2],ch);
            database.createUser(usr[3],ch);
            database.createUser(usr[4],ch);
        }
        catch(ServiceUnavailableException e){
            Log.db(e.getMessage());
        }
    }

    @Test
    public void testResetDatabase(){
        try {
            database.createNode("Nautilus",true);
            database.resetDatabase();
            database.createNode("Nautilus",true);
        }
        catch(ServiceUnavailableException | DatabaseException e){
            Log.info(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testLookUpNode(){
        String label = "Node";
        Assert.assertEquals("Should not find the Node!"
                , false,
                database.lookUpNode("Maokai", label, channelName));
        try {
            database.createNode("Maokai",true);
            database.createNode("nau tilus",true);
        }catch(DatabaseException e){
            e.getMessage();
            Assert.fail();
        }
        Assert.assertEquals("lookUp could not find the node!"
                , true,
                database.lookUpNode("Maokai", label, channelName));

        //lookup with whitespaces
        Assert.assertEquals("lookUp could not find the node!"
                , true,
                database.lookUpNode("Nautilus", label, channelName));

        String userLabel = "userNode";
        //lookup with whitespaces
        String test = "Manuel";
        Assert.assertEquals("lookUp could find the node " + test + "!", false,
                database.lookUpNode(test, userLabel, channelName));

    }

    @Test
    public void validate(){
        int score = 5 ;
        database.setSimulation(false);
        ArrayList<Neo4jWrapper.Pair> validation = database.getValidationForGiver();
        database.validateNode(validation.get(0).getFirst().toString(),score);
        database.validateConnectionTaboo(validation.get(1).getFirst().toString()
                ,validation.get(1).getSecond().toString(),score);
        database.validateConnectionCategory(validation.get(2).getFirst().toString()
                ,validation.get(2).getSecond().toString(),score);
        //database.validateConnection();
    }

    @Test
    public void createQuestion(){
        database.createQuestion("sivir","is it blue?","no");
    }

    @Test
    public void testCreateRelationship(){
        Assert.assertEquals("No such relationship could be created!"
                , true,
                database.insertNodesAndRelationshipIntoOntology("Nautilus", "Hallo", true,
                        "IS", false,""));
        //test increase of rating And false flag override
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                "IS RELATED TO",false,"");
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                "IS RELATED TO",false,"");
        database.insertNodesAndRelationshipIntoOntology("Nautilus","Hallo",true,
                "IS NOT RELATED TO",false,"");
    }

    @Test
    public void testClearRelationships(){
        Assert.assertEquals("No such relationshio could be created!"
                , true,
                database.insertNodesAndRelationshipIntoOntology("Nautilus", "Nautilus", true,
                        "RATING", true,""));
        Assert.assertEquals("Something went wrong when clearing the ratings!"
                , true,
                database.clearFailedRelationships());
    }

    @Test
    public void testGetUserPoints(){
        String user ="John";
        database.createUser(user,channelName);

        Assert.assertEquals("Points were incorrect!",
                0, database.getUserPoints(user, channelName));
        Assert.assertEquals("Points were incorrect!",
                200, database.updateUserPoints(user, 200, channelName));
        Assert.assertEquals("Points were incorrect!",
                200, database.getUserPoints(user, channelName));
    }

    @Test
    public void testGetUserRankings(){
        LinkedHashMap<String,Integer> list = database.getHighScoreList(3,channelName);
        Log.info(list.toString());
    }

    @Test
    public void testGetStreamRankings(){
        database.createUser("John","streamplaystaboo");
        database.createUser("John","realwasabimc");
        database.createUser("John","pewhtv");
        database.createUser("Matthew","pewhtv");
        database.updateUserPoints("John",200,"streamplaystaboo");
        database.updateUserPoints("John",100,"realwasabimc");
        database.updateUserPoints("John",50,"pewhtv");
        database.updateUserPoints("Matthew",70,"pewhtv");


        LinkedList<Neo4jWrapper.StreamerHighscore> result = database.getStreamHighScore();
        StreamRankingContainer sh = new StreamRankingContainer(database.getStreamHighScore());
        System.out.println(sh);
    }

    @Test
    public void testGetUserErrors(){
        String user ="Manuel";
        database.createUser(user,channelName);

        Assert.assertEquals("Points were incorrect!",
                0, database.getUserError(user, channelName));
        Assert.assertEquals("Points were incorrect!",
                1, database.increaseUserError(user, channelName));
    }

    @Test
    public void testVoteKicked(){
        String user ="Manuel";
        database.setUserErrorTimeStamp("Manuel",new Date());
        database.createUser(user,channelName);
        database.updateUserVoteKicked("Manuel",channelName);

    }

    @Test
    public void testGetExplainWord(){
        String explain = "";
        String category = "simulation";
        Set<String> usedWords = new HashSet<>();

        //test taboo word as explain word
        try {
            database.createNode("nautilus",false);
            Assert.assertEquals("Should not retrieve an explain word!", "", database.getExplainWord(category, usedWords));
        }catch(DatabaseException e){
            Log.db(e.getMessage());
        }

        //test category as explain word
        try {
            database.createNode("maokai",false);
            database.setCategory("maokai");
            Assert.assertEquals("Should retrieve an explain word!", "maokai", database.getExplainWord(category, usedWords));
            usedWords.add("maokai");
            database.getExplainWord(category,usedWords);
            Assert.fail();
        }catch(DatabaseException e){
            Log.db(e.getMessage());
        }

        //Test among all nodes
        try {
            database.createNode("alistar",true);
            Assert.assertEquals("Should retrieve an explain word!", "alistar", database.getExplainWord(category, usedWords));
            usedWords.add("alistar");
            database.getExplainWord(category,usedWords);
            Assert.fail();
        }catch(DatabaseException e){
            Log.db(e.getMessage());
        }

    }

    @Test
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
            Log.db(e.getMessage());
        }
    }

    @Test
    public void testSetCategory(){
        database.resetDatabase();
        database.resetRelationships();
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

        database.insertNodesAndRelationshipIntoOntology(explain[0],category,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology(explain[1],category,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology(explain[2],category,true,relation1,true,"");
    }

    @Test
    public void testReliableChange(){
        //Test if reliableflag changes with more than three entries


    }

    @Test
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
        Assert.assertTrue("given Categories are in size not equal 3", result.size() == 3);
    }

    @Test
    public void testGetTabooWords(){

        String explain = "Mario";
        String relation1 = "It appears in ";
        Set<String> result = new HashSet<>();
        int i = 3 ;
        database.setSimulation(true);

        database.insertNodesAndRelationshipIntoOntology("Peach",explain,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology("Peach",explain,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology("Bowser",explain,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology("Luigi",explain,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology("Toad",explain,true,relation1,true,"");
        database.insertNodesAndRelationshipIntoOntology("Toad",explain,true,relation1,true,"");

        result = database.getTabooWords(explain,"",i); //fetch three words
        for(String r:result){
            Assert.assertFalse(r.equals("bowser"));
        }
    }

    @Test
    public void testGetUserTimeoutStamp(){
        database.createUser("John","streamplaystaboo");
        database.setUserErrorTimeStamp("John",new Date());
        String[] result = database.getUserErrorTimeStamp("John");

        Log.info(result.toString());
    }

    @Test
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
                    targetNode,isExplain, relation,true,"");
        }


    }

    @Test
    public void testSetUpNodes() {
        database.resetRelationships();
        database.resetDatabase();

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {

            String sCurrentLine = br.readLine();
            sCurrentLine = br.readLine();

            int i = 0 ;
            while (!sCurrentLine.startsWith("CreateNodesAndRelationships:")) {
                try {
                    database.createNode(sCurrentLine, true);
                    i++;
                    if(i == 7) database.setSimulation(false);
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
                    if(i == 6) database.setSimulation(true);
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

}
