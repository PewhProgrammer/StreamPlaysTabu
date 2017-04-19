package module;

import gui.GuiAnchor;
import javafx.application.Platform;
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
        ga.setModel(gm);
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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GuiAnchor.cont.onNotifyRegistrationTime();
            }
        });

        try {
            Thread.sleep(32000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GuiAnchor.gameModel.setGameState(GameState.GameStarted);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GuiAnchor.cont.onNotifyGameState();
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //add guesses, explanations etc...
        GuiAnchor.gameModel.addExplanation("It is sweet.");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GuiAnchor.cont.onNotifyExplanation();
            }
        });

        //simulate realism LOL
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GuiAnchor.gameModel.guess("banane");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GuiAnchor.cont.onNotifyGuess();
            }
        });

        //simulate realism LOL
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GuiAnchor.gameModel.guess("schaschlik");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GuiAnchor.cont.onNotifyGuess();
            }
        });

        //simulate realism LOL
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GuiAnchor.gameModel.addExplanation("It used to be trololol");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GuiAnchor.cont.onNotifyExplanation();
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GuiAnchor.gameModel.addQAndA("Is it sexy?", "Yes it is very sexy!");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GuiAnchor.cont.onNotifiyQandA();
            }
        });

        while(t.isAlive()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
