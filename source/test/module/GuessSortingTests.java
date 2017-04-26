package module;

import model.GameModel;
import model.Guess;
import model.Language;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lenovo on 26.04.2017.
 */
public class GuessSortingTests extends SampleTest {

    private GameModel gm;

    @org.junit.Test
    public void setUp() {
        short length = 2;
        gm = new GameModel(Language.Eng, length, null, null);
    }

    public void testOrderSimple() {
        List<String> expectedOrder = new LinkedList<>();
        gm.guess("Peter");
        waitNext();
        gm.guess("Karl");
        waitNext();
        gm.guess("Gustav");
        waitNext();
        gm.guess("Marco");

        expectedOrder.add("Marco");
        expectedOrder.add("Gustav");
        expectedOrder.add("Karl");
        expectedOrder.add("Peter");

        checkOrder(expectedOrder);
    }

    private void waitNext() {
        try {
            Thread.sleep(5100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkOrder(List<String> expectedOrder) {
        Iterator<String> itExpected = expectedOrder.iterator();
        Iterator<Guess> itWas = gm.getGuesses().iterator();

        assertEquals(expectedOrder.size(), gm.getGuesses().size());

        while (itExpected.hasNext()) {
            String expected = itExpected.next();
            Guess g2 = itWas.next();
            assertEquals(expected, g2.getGuess());
        }
    }
}
