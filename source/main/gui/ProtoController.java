package gui;

import javafx.application.Platform;
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
import logic.bots.AltTwitchBot;
import logic.bots.BeamBot;
import model.GameState;
import model.Guess;
import model.IObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.ProtoAnchor.cont;
import static gui.ProtoAnchor.gameModel;
import static gui.ProtoAnchor.stage;

/**
 * Created by Marc on 10.04.2017.
 */
public class ProtoController implements Initializable, IObserver {

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

    @FXML
    private Text guesses = new Text();
    @FXML
    private Text explanations = new Text();
    @FXML
    private Text qAndA = new Text();
    @FXML
    private Text timer = new Text();
    @FXML
    private Text gameTimer = new Text();
    @FXML
    private Text giverText = new Text();

    public static boolean fullscreen = false;
    public static int resX = 1280, resY = 720;

    private String platform = "twitch";
    private String chn = "realwasabimc";


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
                System.out.println("Fullscreen = " + ProtoController.fullscreen + "; Resolution: " + ProtoController.resX + "x" + ProtoController.resY);
                System.out.println("Platform: " + platform + ", Channel: " + chn);

                //initialize bots
                if(platform.equals("twitch"))
                    try {
                        //ProtoAnchor.gameModel.setBot(new TwitchBot(ProtoAnchor.gameModel, chn));
                        ProtoAnchor.gameModel.setBot(new AltTwitchBot(ProtoAnchor.gameModel,"#"+chn));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else
                    try {
                      ProtoAnchor.gameModel.setBot(new BeamBot(ProtoAnchor.gameModel, chn));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                Parent root = null;
                try {
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFiles/idle.fxml"));
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFiles/idle.fxml"));
                    root = loader.load();
                    ProtoAnchor.cont = loader.getController();
                    gameModel.updateObserver(cont);
                    onNotifyRegistrationTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, ProtoController.resX, ProtoController.resY);
                stage.setScene(scene);
                stage.centerOnScreen();
                if(ProtoController.fullscreen)
                    stage.setFullScreen(true);

                gameModel.setGameState(GameState.Registration);
            }
        });

        fullscreenBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(fullscreenBox.isSelected()) ProtoController.fullscreen = true;
                else ProtoController.fullscreen = false;
            }
        });

        fullHDRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProtoController.resX = 1920;
                ProtoController.resY = 1080;
                HDRadio.setSelected(false);
            }
        });

        HDRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProtoController.resX = 1280;
                ProtoController.resY = 720;
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

        if (ProtoAnchor.gameModel.getGameState() == GameState.GameStarted) {
            Parent root = null;
            try {
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFiles/game.fxml"));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFiles/game.fxml"));
                  root = loader.load();
                ProtoAnchor.cont = loader.getController();
                gameModel.updateObserver(cont);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root, ProtoController.resX, ProtoController.resY);
            stage.setScene(scene);

            cont.giverText.setText("Giver: " + gameModel.getGiver());

            if(ProtoController.fullscreen)
                stage.setFullScreen(true);

            new Thread() {
                public void run() {
                    for(int i=120; i>=0; i--) {
                            cont.gameTimer.setText(i + "s");
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

        }
        else if(ProtoAnchor.gameModel.getGameState() == GameState.Registration) {
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFiles/idle.fxml"));
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFiles/idle.fxml"));
                root = loader.load();
                ProtoAnchor.cont = loader.getController();
                gameModel.updateObserver(cont);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root, ProtoController.resX, ProtoController.resY);
            stage.setScene(scene);
            if(ProtoController.fullscreen)
                stage.setFullScreen(true);
        }
        else
            System.out.println("GameState unknown for GUI");
    }

    @Override
    public void onNotifiyQandA() {
        String s = "Q. and A.:\n\n";
        for(int i = 0; i < ProtoAnchor.gameModel.getQAndA().size(); i++) {
            String[] QA = ProtoAnchor.gameModel.getQAndA().get(i);
            s = s + "Q: " + QA[0] + "\nA: " + QA[1] + "\n\n";
        }
        qAndA.setText(s);
    }

    @Override
    public void onNotifyCategoryChosen() {

    }

    @Override
    public void onNotifyExplanation() {
        String s = "Explanations:\n\n";
        for(String str : ProtoAnchor.gameModel.getExplanations()) {
            s = s + "- " + str + "\n";
        }
        explanations.setText(s);
    }

    @Override
    public void onNotifyWinner() {

    }

    @Override
    public void onNotifyGuess() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String s = "Guesses:\n\n";
                for(Guess g : ProtoAnchor.gameModel.getGuesses()) {
                    s = s + "- " + g.getGuess() + "\n";
                }
                guesses.setText(s);
            }
        });
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
        new Thread() {
            public void run() {
                for(int i=30; i>=0; i--) {
                    if(i>=10) {
                        final int j = i;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText("00:" + j);
                            }
                        });
                    }
                    else {
                        final int j = i;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText("00:0" + j);
                            }
                        });
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
