package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Marc on 10.04.2017.
 */
public class FXMLController implements Initializable {

    @FXML
    private RadioButton fullHDRadio = new RadioButton();
    @FXML
    private RadioButton HDRadio = new RadioButton();

    @FXML
    private Button startButton = new Button();

    @FXML
    private CheckBox fullscreenBox = new CheckBox();

    @FXML
    private Text text = new Text();

    static boolean fullscreen = false;
    static int resX = 1280, resY = 720;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        HDRadio.setSelected(true);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Starting Game with settings: Fullscreen = " + FXMLController.fullscreen + "; Resolution: " + FXMLController.resX + "x" + FXMLController.resY);

                Parent root = null;
                try {
                    root = FXMLLoader.load((getClass().getResource("/idle.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, FXMLController.resX, FXMLController.resY);
                GuiAnchor.stage.setScene(scene);
                GuiAnchor.stage.centerOnScreen();
                if(FXMLController.fullscreen)
                    GuiAnchor.stage.setFullScreen(true);
            }
        });

        fullscreenBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(fullscreenBox.isSelected()) fullscreen = true;
                else fullscreen = false;
            }
        });

        fullHDRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resX = 1920;
                resY = 1080;
                HDRadio.setSelected(false);
            }
        });

        HDRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resX = 1280;
                resY = 720;
                fullHDRadio.setSelected(false);
            }
        });
    }
}
