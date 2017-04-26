package gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    ToggleButton fullscreenToggle = new ToggleButton();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fullscreenToggle.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("pictures/american-flag.png"))));
    }

    @Override
    public void onNotifyGameState() {

    }

    @Override
    public void onNotifiyQandA() {

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
