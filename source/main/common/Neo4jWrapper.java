package common;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by Thinh-Laptop on 08.04.2017.
 */
public class Neo4jWrapper {

    public Neo4jWrapper(boolean simulation){
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

}
