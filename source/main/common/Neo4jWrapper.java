package common;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private final int DEL_TRESHHOLD = 5;
    private final String label;
    Driver driver;

    public Neo4jWrapper(boolean simulation, String neo4jbind){

        //baut enkryptische Verbindung, um uns gegen "man-in-the-middle" attacken zu schützen
        config = Config.build().withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig() ;
        neo4jbindAddr.append(neo4jbind);
        legacy = simulation;

        driver = acquireDriver("bolt://" + neo4jbindAddr,
                AuthTokens.basic( "neo4j", "streamplaystabu" ),config);

        //non-legacy nodes are used experimentally
        if(simulation){
            resetDatabase();
            label = "Node";
        }
        else label = "legacyNode";

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
        }catch(ServiceUnavailableException e){
            Log.debug(e.getLocalizedMessage());
            return false;
        }
        //creates a binding connection between node1 and node2
        return createRelationship(node1,node2,relationship);
    }

    public int increaseUserPoints(String user, int i)
    throws Neo4jException{
        return getUserPoints(user) + i;
    }

    public int decreaseUserPoints(String user, int i) throws Neo4jException{
        return increaseUserPoints(user,-i);
    }

    public int getUserPoints(String user) throws Neo4jException{
        //parse user points from database
        return 0;
    }

    //** TODO: Missing Logging system for user's activity


    /**************************** INTERN PUBLIC METHOD ****************************/

    /**
     * creates a Node in our ontology. Dependent on legacy or not.
     * @param nodeName
     * @return
     * @throws ServiceUnavailableException
     */
    public boolean createNode(String nodeName) throws ServiceUnavailableException{

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                if(lookUpNode(nodeName)){
                    Log.info(nodeName + " is already in the database!");
                    return false;
                }
                tx.run( "CREATE (a: "+label+" {name: {name} })",
                        parameters( "name", nodeName ) );
                tx.success();
            }

        }


        return true;
    }

    /**
     * look up a node in our ontology
     * @param nodeName
     * @return
     */
    public boolean lookUpNode(String nodeName){
        try ( Session session = driver.session() ) {
            try (Transaction tx = session.beginTransaction()) {

                StatementResult result = tx.run("MATCH (n) WHERE n.name = {name} " +
                                "RETURN n",
                        parameters("name", nodeName));
                if(!result.hasNext()) return false;
                while (result.hasNext()) {
                    Record record = result.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get("name");

                    val.get(0).asNode().labels().forEach(t -> {
                        Log.info("Found Node: " + String.format("%s %s", name,
                                t.toString()));
                    });

                }
            }
        }

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
                tx.run("        MATCH (n:Node)\n" +
                        "        DETACH DELETE n"
                ); // , parameters( "name", nodeName )

                tx.success();
            }

        }
        return true;
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
