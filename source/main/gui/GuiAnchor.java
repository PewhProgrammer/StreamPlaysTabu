package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.GameModel;

/**
 * Created by Marc on 11.04.2017.
 */
public class GuiAnchor extends Application {

    public static Stage stage = null;
    public  GameModel gameModel = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GuiAnchor.stage = primaryStage;
        FXMLController.gm = gameModel;
        //load the start.fxml
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("FXMLFiles/start.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
        FXMLController cont = loader.getController();
        //setup scene
        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("StreamPlaysTabu");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


    }

    public void setModel(GameModel gameModel) {
        gameModel = gameModel;
    }
}
