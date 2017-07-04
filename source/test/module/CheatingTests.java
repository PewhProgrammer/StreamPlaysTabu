package module;

import common.Util;
import junit.framework.TestCase;
import model.GameModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lenovo on 17.05.2017.
 */
public class CheatingTests extends TestCase {

    private GameModel gm;

    @org.junit.Test
    public void setUp() {
        this.gm = new GameModel(null);
    }

    public void testInvalidCharacters() {
        String[] input = { "H@llo", "Lul!", "$tern", "lololoo?lloolo]", "("};
        checkChars(input, false);
    }

    public void testValidCharacters() {
        String[] input = { "Hallo", "gross, dick, sexy", "Vel'Khoz", "rot-gelb" };
        checkChars(input, true);
    }

    public void testInvalidLemma() {
        String[] exps = {"It wears a big, red hat", "It is the brother of luigi", "It is the sister of your running mama"};
        String[] words = {"wearing", "luigi", "peroquet"};
        Set<String> s1 = new HashSet<>();
        Set<String> s2 = new HashSet<>();
        Set<String> s3 = new HashSet<>();
        s2.add("be");
        s3.add("eat");
        s3.add("like");
        s3.add("run");
        ArrayList<Set<String>> taboos = new ArrayList<>();
        taboos.add(s1);
        taboos.add(s2);
        taboos.add(s3);

        checkLemma(exps, taboos, words, false);
    }

    public void testValidLemma() {
        String[] exps = {"It wears a big, red hat", "It is the brother of luigi", "It is the sister of your running mama"};
        String[] words = {"bear", "sister", "walking"};
        Set<String> s1 = new HashSet<>();
        Set<String> s2 = new HashSet<>();
        Set<String> s3 = new HashSet<>();
        s2.add("be");
        s3.add("eat");
        s3.add("like");
        s3.add("run");
        ArrayList<Set<String>> taboos = new ArrayList<>();
        taboos.add(s1);
        taboos.add(s2);
        taboos.add(s3);

        checkLemma(exps, taboos, words, true);
    }

    private void checkLemma(String[] exps, ArrayList<Set<String>> taboos, String words[], boolean valid) {
        int size = exps.length;

        String v = valid == true ? "valid" : "invalid";
        String iv = valid == true ? "invalid" : "valid";

        for (int i = 0; i < size; i++) {
            gm.setExplainWord(words[i]);
            gm.setTabooWords(taboos.get(i));
            assertEquals("'" + exps[i] + "' was rated as " + iv + " but should be " + v + "." ,
                    valid, Util.checkCheating(exps[i], gm));
        }
    }


    private void checkChars(String[] input, boolean valid) {
        String v = valid == true ? "valid" : "invalid";
        String iv = valid == true ? "invalid" : "valid";

        for (String s : input) {
            assertEquals("'" + s + "' was rated as " + iv + " but should be " + v + "." ,
                    valid, Util.checkCheating(s, gm));
        }
    }

    public void testCheckWordUsage() {
        String expl = "Hehehe das ist 1 explanation zum explain word k alist ar";
        gm.setExplainWord("kalista");
        assertTrue(Util.checkCheating(expl, gm).length() != 0);
    }
}
