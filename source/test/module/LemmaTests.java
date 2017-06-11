package module;

import junit.framework.TestCase;
import model.GameModel;
import model.Language;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tim on 25.04.2017.
 */
public class LemmaTests extends TestCase {

    private GameModel gm;

    @org.junit.Test
    public void setUp() {
        this.gm = new GameModel(null);
    }

    public void testEnSimple() {
        List<String> lemmas = new LinkedList<>();
        lemmas.add("dog");
        lemmas.add("run");
        lemmas.add("start");
        lemmas.add("check");
        lemmatize("dogs running started checks", lemmas);
    }

    public void testEnIrregular() {
        List<String> lemmas = new LinkedList<>();
        lemmas.add("be");
        lemmas.add("blow");
        lemmas.add("rise");
        lemmas.add("sell");
        lemmatize("was blew rose sold", lemmas);
    }

    public void testAntiWord() {
        List<String> lemmas = new LinkedList<>();
        lemmas.add("afk");
        lemmatize("afk", lemmas);
    }

    private void lemmatize(String input, List<String> lemmas) {
        List<String> l = gm.lemmatize(input);
        assertEquals(l.size(), lemmas.size());

        Iterator<String> itl = l.iterator();
        Iterator<String> itlemmas = lemmas.iterator();

        while (itl.hasNext()) {
            String s1 = itl.next();
            String s2 = itlemmas.next();

            assertEquals(s1, s2);
        }
    }
}
