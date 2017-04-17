package module;

import common.Log;
import common.Neo4jWrapper;
import junit.framework.TestCase;
import model.Language;
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

    @org.junit.Test
    public void setUp() throws Exception {
        database = new Neo4jWrapper(simulation,neo4jbindAddr);
        database.resetDatabase();
    }

    public void testCreateNode(){
        try {
            database.createNode("Nautilus");
            assertEquals("No such Node found in database!"
                    ,true,
                    database.lookUpNode("Nautilus"));
        }
        catch(ServiceUnavailableException e){
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testResetDatabase(){
        database.createNode("Maokai");
        database.resetDatabase();
        assertEquals("Should not find the Node!"
                ,false,
                database.lookUpNode("Maokai"));
    }

    public void testLookUpNode(){
        assertEquals("Should not find the Node!"
                ,false,
                database.lookUpNode("Maokai"));
        database.createNode("Maokai");
        assertEquals("No such Node found in database!"
                ,true,
                database.lookUpNode("Maokai"));
    }

    public void testCreateRelationship(){
        database.createRelationship("Nautilus","Nautilus",
                "KNOWS");
    }

}
