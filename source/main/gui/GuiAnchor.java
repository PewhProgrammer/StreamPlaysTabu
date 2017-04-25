package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import model.GameModel;

/**
 * Created by Marc on 24.04.2017.
 */
public class GuiAnchor extends Application {

    public static Stage stage = null;
    public static ProtoController cont = null;
    public static GameModel gameModel = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public void setGameModel(GameModel gm) {
        GuiAnchor.gameModel = gm;
    }
}
