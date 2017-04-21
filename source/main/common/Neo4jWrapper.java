package common;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.*;

import java.io.File;
import java.util.*;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by Thinh-Laptop on 08.04.2017.
 */
public class Neo4jWrapper {

    private final Config config ;
    //default -> localhost:7687
    //You're using the wrong port number. 7474 is by default used for http
    // whereas 7687 is the default for binary bolt protocol.
    private StringBuilder neo4jbindAddr = new StringBuilder();
    private final boolean legacy ;
    private String userLabel = "userNode";
    private final int DEL_TRESHHOLD = 5;
    private final String label;
    private final Driver driver;
    private final Random randomizer;


    public Neo4jWrapper(boolean simulation, String neo4jbind,int seed){

        //baut enkryptische Verbindung, um uns gegen "man-in-the-middle" attacken zu schützen
        config = Config.build().withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig() ;
        neo4jbindAddr.append(neo4jbind);
        legacy = simulation;
        randomizer = new Random(seed);

        driver = acquireDriver("bolt://" + neo4jbindAddr,
                AuthTokens.basic( "neo4j", "streamplaystabu" ),config);

        //non-legacy nodes are used experimentally
        if(simulation){
            //resetDatabase();
            label = "Node";
            userLabel = "userNode";
        }
        else {label = "legacyNode";
        userLabel = "legacyUserNode";}

    }

    /**
     * This method is mainly called to build our ontology
     * @param node1
     * @param node2
     * @param relationship
     * @return
     */
    public boolean insertNodesAndRelationshipIntoOntology(String node1, String node2, String relationship){
        //create two nodes if not already
        try {
            createNode(node1);
            createNode(node2);
        }catch(ServiceUnavailableException | DatabaseException e){
            Log.debug(e.getLocalizedMessage());
            return false;
        }
        //creates a binding connection between node1 and node2
        return createRelationship(node1,node2,relationship);
    }

    public int updateUserPoints(String user, int i) {

        try {
            return updateUserPropertiesFromDatabase(user,"points",i);
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user);
        }
        return 0; //if new user is created
    }


    /**
     * if no user was found, then a user will be created
     * @param user
     * @return Points of user as in database
     * @throws Neo4jException
     */
    public int getUserPoints(String user) throws Neo4jException{
        try {
            return fetchUserPropertiesFromDatabase(user,"points");
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user);
        }
        return 0; //if new user is created
    }

    /**
     * Increase the error any user made by one
     * @param user
     * @return updated count of mistakes the user now have
     * @throws Neo4jException
     */
    public int increaseUserError(String user) throws Neo4jException{
        try {
            return updateUserPropertiesFromDatabase(user,"mistakes",1);
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user);
        }
        return 0; //if new user is created
    }

    public int getUserError(String user) throws Neo4jException{
        try {
            return fetchUserPropertiesFromDatabase(user,"mistakes");
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user);
        }
        return 0; //if new user is created
    }

    /**
     * retrieves categories for pre-voting
     * @param i capacity of categories
     * @return
     */
    public Set<String> getCategories(int i){
        return fetchFilteredCategoryFromDatabase(i);
    }

    /**
     * randomly fetch high-validated words connected to the category
     * @param category from which the explain word inherits
     * @return
     */
    public String getExplainWord(String category,Set<String> usedWords) throws DatabaseException
    {
        return fetchConnectedWordFromDatabase(category);
    }


    /**
     * UserNode will be created in Database, if not already existing
     * @param str
     */
    public void createUser(String str){
        try {
            generateUserNodeInDatabase(str);
            Log.trace("Created Node: \""+str+"\" userNode");
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }
    }

    //** TODO: Missing Logging system for user's activity


    /**************************** INTERN CYPHER MANAGEMENT METHOD ****************************/

    /**
     * creates a Node in our ontology. Dependent on legacy or not.
     * @param nodeName
     * @return
     * @throws ServiceUnavailableException
     */
    public void createNode(String nodeName) throws ServiceUnavailableException,DatabaseException{

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                if(lookUpNode(nodeName,"legacyNode") || lookUpNode(nodeName,"Node")){
                    throw new DatabaseException("Node: " + nodeName + " is already in the database!");
                }
                tx.run( "CREATE (a: "+label+" {name: {name} })",
                        parameters( "name", nodeName ) );
                tx.success();
            }

        }

        Log.trace("Created Node: " + nodeName +" "+label);
        return;
    }

    /**
     * look up a node in our ontology
     * @param nodeName
     * @return
     */
    public boolean lookUpNode(String nodeName, String label){
        StringBuilder builder = new StringBuilder();
        try ( Session session = driver.session() ) {
            try (Transaction tx = session.beginTransaction()) {

                StatementResult result = tx.run("MATCH (n:"+label+") WHERE n.name = {name} " +
                                "RETURN n",
                        parameters("name", nodeName));
                if(!result.hasNext()) return false;
                while (result.hasNext()) {
                    Record record = result.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get("name");

                    val.get(0).asNode().labels().forEach(t -> {
                        builder.append("Found Node: " + String.format("%s %s ", name,
                                t.toString()));
                    });

                }

            }
        }
        Log.trace(builder.toString());

        return true;
    }

    //TODO
    public List<Node> getAllNodes(){
        List<Node> nodes = new ArrayList<>();
        try ( Session session = driver.session() ) {
            try (Transaction tx = session.beginTransaction()) {

                StatementResult result = tx.run("MATCH (n) RETURN n");
                while (result.hasNext()) {
                    Record record = result.next();
                    System.out.println(String.format("%s %s", record.get("title").asString(), record.get("name").asString()));
                    Object test = record.asMap();
                    nodes.add(new Node());
                }
            }
        }

        return nodes;

    }

    /**
     * Creates a connection between two nodes while also incrementing the rating by 1
     * @param node1
     * @param node2
     * @param relationship
     * @return
     */
    public boolean createRelationship(String node1, String node2, String relationship){
        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                int count = 0;
                StatementResult result = tx.run( "MATCH (n)-[rel:rating]->(r)" +
                        "RETURN rel");
                while (result.hasNext()) {
                    Record record = result.next();
                    count = record.get("rating").asInt();
                }

                tx.run( "MATCH (ee) WHERE ee.name =  {name1} "+
                                "MATCH (js) WHERE ee.name = {name2} " +
                        "CREATE UNIQUE (ee)-[:"+relationship+" {rating: "+ ++count +"," +
                                "legacy: "+legacy+"} " +
                                "]->(js)"

                        , parameters( "name1",node1,"name2",node2 )); //

                tx.success();
                return true;
            }

        }

    }

    public boolean createProperty(){
        //Not needed up till now
        return false;
    }

    /**
     * clear all connections that have too less of a rating
     * @return true if succesfully delete the relationship
     */
    public boolean clearFailedRelationships(){

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run( "MATCH (n)-[rel]->(r) \n" +
                                "WHERE rel.rating < "+DEL_TRESHHOLD+"\n" +
                                "DELETE rel");

                tx.success();
                return true;
            }

        }
    }

    /**
     * Resets all connections to the pre-existing legacy edges.
     * Anything besides of legacy = true, will be cleared
     * @return
     */
    public boolean resetRelationships(){

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run( "MATCH (n)-[rel]->(r) \n" +
                        "WHERE NOT EXISTS (rel.legacy) OR NOT (rel.legacy = true) " +
                        "DELETE rel");

                tx.success();
                return true;
            }
        }
    }

    /**
     * Does only delete normal nodes but not legacy nodes
     * @return suceed if nodes are deleted
     */
    public boolean resetDatabase(){
        resetRelationships();
        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run("MATCH (n)\n" +
                        "WHERE NOT n:legacyUserNode AND NOT n:legacyNode" +
                        "DETACH DELETE n"
                ); // , parameters( "name", nodeName )

                tx.success();
            }

        }
        return true;
    }

    /**
     * Cypher request for all categories with high validation
     * @return
     */
    public Set<String> fetchFilteredCategoryFromDatabase(int cap){
        Set<String> result = new HashSet<>(cap);

        try ( Session session = driver.session() )
        {
            //TODO: CYPHER STATEMENT REQUEST
            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run(""
                ); // , parameters( "name", nodeName )

                tx.success();
            }

        }

        return result ;
    }

    private int fetchUserPropertiesFromDatabase(String user,String property) throws DatabaseException{
        int result = 0 ;
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                StatementResult sResult = tx.run("MATCH (n:"+userLabel+") WHERE n.name = {name} " +
                                "RETURN n",
                        parameters("name",user));
                if(!sResult.hasNext()) throw new DatabaseException("No User "+user+" found!");
                while (sResult.hasNext()) {
                    Record record = sResult.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get(property);
                    result = name.asInt();

                    builder.append("Fetched "+property+": " + String.format("%s %s ", user,
                            result));
                }
            }
        }
        Log.trace(builder.toString());
        return result ;
    }

    private int updateUserPropertiesFromDatabase(String user,String property,int i) throws DatabaseException{
        int oldPoints = fetchUserPropertiesFromDatabase(user,property) ;
        int result = oldPoints + i ;
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                StatementResult sResult = tx.run("MATCH (n:"+userLabel+") WHERE n.name = {name} " +
                                "SET n."+property+"={propertyvalue}" +
                                "RETURN n",
                        parameters("name",user,"propertyvalue",result));


                builder.append("Updated "+property+": " + String.format("%s -> %s %s", oldPoints,
                            result ,user));
            }
        }
        Log.trace(builder.toString());
        return result ;
    }

    private void generateUserNodeInDatabase(String user) throws DatabaseException{

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                if(lookUpNode(user,userLabel)){
                    throw new DatabaseException("User "+ user + " is already in the database!");
                }

                tx.run( "CREATE (a: "+userLabel+" {name: {name}," +
                                "points: 0,mistakes: 0 })",
                        parameters( "name", user,"points",0 ) );
                tx.success();
            }

        }

        return ;
    }

    /**
     * Take the best ten arc from category and choose arbitrarily
     * @param category
     * @return
     */
    private String fetchConnectedWordFromDatabase(String category) throws DatabaseException{
        StringBuilder builder = new StringBuilder();
        String result = "";

        if(category.equals("simulation")){
            try ( Session session = driver.session() )
            {
                try ( Transaction tx = session.beginTransaction() )
                {

                    StatementResult sResult = tx.run( "MATCH (n:Node) RETURN n");

                    if(!sResult.hasNext())
                        throw new DatabaseException("No Explain Word available!");
                    List<Record> list = sResult.list();
                    Record record = list.get(randomizer.nextInt(list.size()));

                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get("name");
                    result = name.toString();
                    result = result.replaceAll("\"", "");

                    builder.append("Fetched ExplainWord: " + String.format("%s", result));
                    tx.success();
                }

            }
        }

        Log.info(builder.toString());
        return result;
    }

    private Driver acquireDriver(String uri, AuthToken authToken, Config config)
    {

        try {
            return GraphDatabase.driver(uri, authToken, config);
        }
        catch (ServiceUnavailableException ex)
        {
            Log.debug("No valid database URI found");
            System.exit(0);
        }
        return null;

    }

    public class Node{

    }

}
