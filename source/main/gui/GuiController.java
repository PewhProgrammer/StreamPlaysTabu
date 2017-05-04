package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.IObserver;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Marc on 24.04.2017.
 */
public class GuiController implements Initializable, IObserver {

    @FXML
    ComboBox fullscreenComboBox = new ComboBox();
    @FXML
    ComboBox platformComboBox = new ComboBox();
    @FXML
    ComboBox gamemodeComboBox = new ComboBox();

    @FXML
    TextField channelNameField = new TextField();

    @FXML
    Button startGameButton = new Button();

    private boolean fullscreen = true;
    private String platform = "Twitch", channel = "k3uleee", gamemode = "Free for all";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fullscreenComboBox.getItems().addAll("On", "Off");
        platformComboBox.getItems().addAll("Twitch", "Beam");
        gamemodeComboBox.getItems().addAll("Free for all", "Streamer Explains");

        fullscreenComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(fullscreenComboBox.getValue().toString().equals("On"))
                    fullscreen = true;
                else
                    fullscreen = false;
            }
        });

        platformComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                platform = platformComboBox.getValue().toString();
            }
        });

        channelNameField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                channel = channelNameField.getText();
            }
        });

        gamemodeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gamemode = gamemodeComboBox.getValue().toString();
            }
        });
    }

    @Override
    public void onNotifyGameState() {

    }

    @Override
    public void onNotifyQandA() {

    }

    @Override
    public void onNotifyCategoryChosen() {

    }

    @Override
    public void onNotifyExplanation() {

    }

    @Override
    public void onNotifyWinner() {

    }

    @Override
    public void onNotifyGuess() {

    }

    @Override
    public void onNotifyScoreUpdate() {

    }

    @Override
    public void onNotifyGameMode() {

    }

    @Override
    public void onNotifyKick() {

    }

    @Override
    public void onNotifyRegistrationTime() {

    }
}
