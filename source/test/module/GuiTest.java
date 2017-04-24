package module;

import gui.ProtoAnchor;
import javafx.application.Platform;
import junit.framework.TestCase;
import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 18.04.2017.
 */
public class GuiTest extends TestCase {

    GameModel gm;
    ProtoAnchor ga;

    @org.junit.Test
    public void setUp() throws Exception {
        gm = new GameModel(null, (short)2, null, null);
        ga = new ProtoAnchor();
    }

    public void test() {
        ga.setModel(gm);
        ProtoAnchor.gameModel.setGiver("k3uleee");

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
                ProtoAnchor.cont.onNotifyRegistrationTime();
            }
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProtoAnchor.gameModel.setGameState(GameState.GameStarted);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ProtoAnchor.cont.onNotifyGameState();
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //add guesses, explanations etc...
        ProtoAnchor.gameModel.addExplanation("It is sweet.");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ProtoAnchor.cont.onNotifyExplanation();
            }
        });

        //simulate realism LOL
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProtoAnchor.gameModel.guess("banane");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ProtoAnchor.cont.onNotifyGuess();
            }
        });

        //simulate realism LOL
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProtoAnchor.gameModel.guess("schaschlik");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ProtoAnchor.cont.onNotifyGuess();
            }
        });

        //simulate realism LOL
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProtoAnchor.gameModel.addExplanation("It used to be trololol");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ProtoAnchor.cont.onNotifyExplanation();
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProtoAnchor.gameModel.addQAndA("Is it sexy?", "Yes it is very sexy!");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ProtoAnchor.cont.onNotifiyQandA();
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
