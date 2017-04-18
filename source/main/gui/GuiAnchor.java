package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Marc on 11.04.2017.
 */
public class GuiAnchor extends Application {

    public static Stage stage = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GuiAnchor.stage = primaryStage;
        //load the start.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
        //setup scene
        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("StreamPlaysTabu");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void waitingForPlayer() throws Exception {
        Parent root = FXMLLoader.load((getClass().getResource("/idle.fxml")));
        Scene scene = new Scene(root, FXMLController.resX, FXMLController.resY);
        stage.setScene(scene);
        if(FXMLController.fullscreen)
            stage.setFullScreen(true);
    }
}
