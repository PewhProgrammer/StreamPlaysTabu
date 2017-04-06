import common.Log;
import gui.GuiAnchor;
import logic.GameControl;
import logic.commands.Answer;
import model.GameModel;
import model.Language;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class Main {

    public static void main(String[] args){
        //process command lines

        /**
         * Gui musst config fürn server bestimmen
         */

        Log.info("Launching Server...");
        new GameControl((short)2,Language.Ger).waitingForPlayers();


        String[] param = {"lol"};
        new GuiAnchor().main(param);
        Log.info("Launching GUI...");
        // ....
    }
}
