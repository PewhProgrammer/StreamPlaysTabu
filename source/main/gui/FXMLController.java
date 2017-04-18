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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import logic.bots.BeamBot;
import logic.bots.TwitchBot;
import model.GameModel;
import model.IObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static gui.GuiAnchor.stage;

/**
 * Created by Marc on 10.04.2017.
 */
public class FXMLController implements Initializable, IObserver {

    @FXML
    private RadioButton fullHDRadio = new RadioButton();
    @FXML
    private RadioButton HDRadio = new RadioButton();
    @FXML
    private RadioButton beamRadio = new RadioButton();
    @FXML
    private RadioButton twitchRadio = new RadioButton();

    @FXML
    private Button startButton = new Button();

    @FXML
    private CheckBox fullscreenBox = new CheckBox();

    @FXML
    private TextField channelInput = new TextField();

    private boolean fullscreen = false;
    private int resX = 1280, resY = 720;

    private String platform = "twitch";
    private String chn = "k3uleeeBot";

    public static GameModel gm = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        HDRadio.setSelected(true);
        twitchRadio.setSelected(true);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!channelInput.getText().equals(""))
                    chn = channelInput.getText();

                System.out.println("Starting Game with settings:");
                System.out.println("Fullscreen = " + fullscreen + "; Resolution: " + resX + "x" + resY);
                System.out.println("Platform: " + platform + ", Channel: " + chn);

                //initialize bots
                if(platform.equals("twitch"))
                    try {
                    //  gm.setBot(new TwitchBot(/*chn*/));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else
                    try {
                    //  gm.setBot(new BeamBot(chn));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                Parent root = null;
                try {
                    root = FXMLLoader.load((getClass().getResource("FXMLFiles/idle.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, resX, resY);
                stage.setScene(scene);
                stage.centerOnScreen();
                if(fullscreen)
                    stage.setFullScreen(true);
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

        twitchRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                platform = "twitch";
                beamRadio.setSelected(false);
            }
        });

        beamRadio.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                platform = "beam";
                twitchRadio.setSelected(false);
            }
        }));
    }


    @Override
    public void onNotifyGameState() {
        switch (gm.getGameState()) {

            case GameStarted: {
                Parent root = null;
                try {
                    root = FXMLLoader.load((getClass().getResource("FXMLFiles/game.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, resX, resY);
                stage.setScene(scene);
            }
                break;

            case Registration: {
                Parent root = null;
                try {
                    root = FXMLLoader.load((getClass().getResource("FXMLFiles/idle.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, resX, resY);
                stage.setScene(scene);
            }
                break;
            default:
                System.out.println("GameState unknown for GUI");
        }
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

}
