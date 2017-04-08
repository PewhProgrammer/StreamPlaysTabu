package common;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

import java.io.File;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by Thinh-Laptop on 08.04.2017.
 */
public class Neo4jWrapper {

    private final Config config ;

    public Neo4jWrapper(boolean simulation){

        //baut enkryptische Verbindung, um uns gegen "man-in-the-middle" attacken zu schützen
        config = Config.build().withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig() ;


        if(simulation){
            //clear default data base
            // use default data base
        }
        else{

        }
    }

    //muss noch schauen, ob es sich lohnt Objekte dafür zu machen
    public boolean createNode(){
        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "neo4j" ) );

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run( "CREATE (a:Person {name: {name}, title: {title}})",
                        parameters( "name", "Arthur", "title", "King" ) );
                tx.success();
            }

            try ( Transaction tx = session.beginTransaction() )
            {
                StatementResult result = tx.run( "MATCH (a:Person) WHERE a.name = {name} " +
                                "RETURN a.name AS name, a.title AS title",
                        parameters( "name", "Arthur" ) );
                while ( result.hasNext() )
                {
                    Record record = result.next();
                    System.out.println( String.format( "%s %s", record.get( "title" ).asString(), record.get( "name" ).asString() ) );
                }
            }

        }

        driver.close();

        return true;
    }

    public boolean createRelationship(){
        return false;
    }

    public boolean createProperty(){
        return false;
    }

    //
    public boolean deleteNode(){
        return false;
    }

    public boolean resetDatabase(){
        return false;
    }

    private Driver acquireDriver(List<String> uris, AuthToken authToken, Config config)
    {
        for (String uri : uris)
        {
            try {
                return GraphDatabase.driver(uri, authToken, config);
            }
            catch (ServiceUnavailableException ex)
            {
                // This URI failed, so loop around again if we have another.
            }
        }
        throw new ServiceUnavailableException("No valid database URI found");
    }

}
