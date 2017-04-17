package common;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

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
    private final String label;
    Driver driver;

    public Neo4jWrapper(boolean simulation, String neo4jbind){

        //baut enkryptische Verbindung, um uns gegen "man-in-the-middle" attacken zu schützen
        config = Config.build().withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig() ;
        neo4jbindAddr.append(neo4jbind);
        legacy = simulation;

        driver = acquireDriver("bolt://" + neo4jbindAddr,
                AuthTokens.basic( "neo4j", "streamplaystabu" ),config);

        if(simulation){
            resetDatabase();
            label = "Node";
        }
        else label = "legacyNode";

    }

    //muss noch schauen, ob es sich lohnt Objekte dafür zu machen
    public boolean createNode(String nodeName) throws ServiceUnavailableException{

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run( "CREATE (a: "+label+" {name: {name} })",
                        parameters( "name", nodeName ) );
                tx.success();
            }

        }


        return true;
    }

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

    public boolean createRelationship(String node1, String node2, String relationship){
        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                int count = 0;
                tx.run( "MATCH (ee:Node) WHERE ee.name = " + node1 + " " +
                                "MATCH (js:Node) WHERE ee.name = " + node2 + " " +
                        "(ee)-[:{relationship} {rating: +"+ count++ +"}]->(js)"
                        , parameters( "relationship", relationship )); //

                tx.success();
            }

        }

        return false;
    }

    public boolean createProperty(){
        //sowas wie validationrating für eine beziehung
        return false;
    }

    //
    public boolean deleteNode(){
        //wenn beziehungen zu wenig validierung haben
        return false;
    }

    /**
     * Does only delete normal nodes but not legacy nodes
     * @return suceed if nodes are deleted
     */
    public boolean resetDatabase(){
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
