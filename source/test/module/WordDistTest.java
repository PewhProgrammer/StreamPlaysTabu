package module;

import common.Util;
import junit.framework.TestCase;


/**
 * Created by Tim on 19.04.2017.
 */
public class WordDistTest extends TestCase{

    @org.junit.Test
    public void testDistEqualLength() {
        String[] word1 = {"hallo", "peter", "help", ""};
        String[] word2 = {"hallo", "hallo", "herp", ""};
        int[] dist = {0, 5, 1, 0};
        int size = 4;

        computeDist("testDistEqualLength", word1, word2, dist, size);
    }

    @org.junit.Test
    public void testDistShorter() {
        String[] word1 = {"Hall", "Stew", "peep"};
        String[] word2 = {"Hallo", "Speward", "Ritopls"};
        int[] dist = {1, 3, 6};
        int size = 3;

        computeDist("testDistShorter", word1, word2, dist, size);
    }

    @org.junit.Test
    public void testDistLonger() {
        String[] word1 = {"Hallo", "Speward", "Ritopls"};
        String[] word2 = {"Hall", "Stew", "peep"};
        int[] dist = {1, 3, 6};
        int size = 3;

        computeDist("testDistLonger", word1, word2, dist, size);
    }

    private void computeDist(String test, String[] word1, String[] word2, int[] dist, int size) {

        for (int counter = 0; counter < size; counter++) {
            int distance = Util.getWordDist(word1[counter], word2[counter]);
            assertTrue(test + ": " + word1[counter] + ", "+ word2[counter] + " Expected " + dist[counter] + " but was " + distance + ".",
                    dist[counter] == distance);
            counter++;
        }
    }
}
