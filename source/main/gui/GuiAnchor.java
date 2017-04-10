package gui;/**
 * Created by Thinh-Laptop on 26.03.2017.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiAnchor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));

        Scene scene = new Scene(root, 400, 600);

        primaryStage.setTitle("StreamPlaysTabu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
