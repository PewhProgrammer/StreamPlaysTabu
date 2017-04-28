package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.GameModel;

/**
 * Created by Marc on 24.04.2017.
 */
public class GuiAnchor extends Application {

    public static Stage stage = null;
    public static ProtoController cont = null;
    public static GameModel gameModel = null;

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        GuiAnchor.stage = primaryStage;
        //load the start.fxml

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFiles/Final/start.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFILES/start.fxml"));

        Parent root = loader.load();
//        cont = loader.getController();
//        GuiAnchor.gameModel.addObserver(cont);
        //setup scene
        Scene scene = new Scene(root, 1280, 720);
        GuiAnchor.stage.setTitle("StreamPlaysTaboo");
        GuiAnchor.stage.setScene(scene);
        GuiAnchor.stage.show();
    }

    public void setGameModel(GameModel gm) {
        GuiAnchor.gameModel = gm;
    }
}
