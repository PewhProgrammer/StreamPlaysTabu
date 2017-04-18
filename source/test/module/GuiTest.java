package module;

import gui.GuiAnchor;
import junit.framework.TestCase;
import model.GameMode;
import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 18.04.2017.
 */
public class GuiTest extends TestCase {

    GameModel gm;
    GuiAnchor ga;

    @org.junit.Test
    public void setUp() throws Exception {
        gm = new GameModel(null, (short)2, null, null);
        ga = new GuiAnchor();
    }

    public void test() {
      /*  ga.setModel(gm);
        Thread t = new Thread() {
            public void run() {
                String[] param = {"testparam"};
                ga.main(param);
            }
        };
        t.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ga.gameModel.setGameState(GameState.GameStarted);
        ga.cont.onNotifyGameState();
        */
    }


}
