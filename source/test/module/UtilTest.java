package module;

import common.Util;
import junit.framework.TestCase;

/**
 * Created by Thinh-Laptop on 25.04.2017.
 */
public class UtilTest extends TestCase {

    @org.junit.Test
    public void setUp() throws Exception {
    }

    public void testReduceStringToMinimum(){
        //no whitespace
        assertEquals("csgo",Util.reduceStringToMinimum("CSgo"));

        //whitespaces
        assertEquals("cs go",Util.reduceStringToMinimum("CS go"));
        assertEquals("cs go",Util.reduceStringToMinimum("CS    go"));
    }

    public void testGuessEquals(){
        //lower/upper Case
        assertTrue(Util.
                guessEquals("csgo","CSGO"));
        assertTrue(Util.
                guessEquals("CsGo","csgo"));
        //Whitespaces
        assertTrue(Util.
                guessEquals("csgo","cs go"));



        assertTrue(Util.
                guessEquals(" cs:go","CSGO"));
        assertTrue(Util.
                guessEquals("cs-go","CSGO"));
        assertTrue(Util.
                guessEquals("cs!go","CSGO"));
        assertTrue(Util.
                guessEquals("cs?go","CSGO"));
        assertTrue(Util.
                guessEquals("cs#go","CSGO"));


    }
}
