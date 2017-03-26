package module;

import junit.framework.TestCase;
import org.junit.runners.JUnit4;

/**
 * Created by Thinh-Laptop on 27.03.2017.
 */
public class SampleTest extends TestCase{
    @org.junit.Test
    public void setUp() throws Exception {

    }

    public void test(){

    }

    public void testfailure(){
        //Soltle bei euch failen
        assertEquals(2,3);
    }


}