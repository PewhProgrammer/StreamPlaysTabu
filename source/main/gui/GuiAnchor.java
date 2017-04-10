package gui;/**
 * Created by Thinh-Laptop on 26.03.2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiAnchor extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().
                getResource("" +
                        "gui.fxml"));

        Scene scene = new Scene(root, 1280, 720);

        primaryStage.setTitle("StreamPlaysTabu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
