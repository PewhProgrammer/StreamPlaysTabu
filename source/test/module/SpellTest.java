package module;

import junit.framework.TestCase;
import model.GameModel;
import model.Language;

/**
 * Created by Lenovo on 25.04.2017.
 */
public class SpellTest extends TestCase {

    private GameModel gm;

    public void testEnSimple() {
        short length = 2;
        this.gm = new GameModel(Language.Eng, length, null, null);
        checkSpelling("paernts");
        checkSpelling("smrat cast");
        checkSpelling("afk");
        checkSpelling("dsaodjasio");
    }

    public void testGerSimple() {
        short length = 2;
        this.gm = new GameModel(Language.Ger, length, null, null);
        checkSpelling("Hunt");
        checkSpelling("Tretis");
        checkSpelling("Karte");
        checkSpelling("map");
    }

    private void checkSpelling(String text) {
        gm.checkSpelling(text);
    }
}
