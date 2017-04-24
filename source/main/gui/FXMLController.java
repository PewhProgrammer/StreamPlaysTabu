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
import logic.bots.TwitchBot;
import model.GameState;
import model.Guess;
import model.IObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.GuiAnchor.cont;
import static gui.GuiAnchor.gameModel;
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
                System.out.println("Fullscreen = " + FXMLController.fullscreen + "; Resolution: " + FXMLController.resX + "x" + FXMLController.resY);
                System.out.println("Platform: " + platform + ", Channel: " + chn);

                //initialize bots
                if(platform.equals("twitch"))
                    try {
                        //GuiAnchor.gameModel.setBot(new TwitchBot(GuiAnchor.gameModel, chn));
                        GuiAnchor.gameModel.setBot(new AltTwitchBot(GuiAnchor.gameModel,"#"+chn));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else
                    try {
                      GuiAnchor.gameModel.setBot(new BeamBot(GuiAnchor.gameModel, chn));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                Parent root = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFiles/idle.fxml"));
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFiles/idle.fxml"));
                    root = loader.load();
                    GuiAnchor.cont = loader.getController();
                    gameModel.updateObserver(cont);
                    onNotifyRegistrationTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, FXMLController.resX, FXMLController.resY);
                stage.setScene(scene);
                stage.centerOnScreen();
                if(FXMLController.fullscreen)
                    stage.setFullScreen(true);

                gameModel.setGameState(GameState.Registration);
            }
        });

        fullscreenBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(fullscreenBox.isSelected()) FXMLController.fullscreen = true;
                else FXMLController.fullscreen = false;
            }
        });

        fullHDRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLController.resX = 1920;
                FXMLController.resY = 1080;
                HDRadio.setSelected(false);
            }
        });

        HDRadio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLController.resX = 1280;
                FXMLController.resY = 720;
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

        if (GuiAnchor.gameModel.getGameState() == GameState.GameStarted) {
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFiles/game.fxml"));
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFiles/game.fxml"));
                  root = loader.load();
                GuiAnchor.cont = loader.getController();
                gameModel.updateObserver(cont);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root, FXMLController.resX, FXMLController.resY);
            stage.setScene(scene);

            cont.giverText.setText("Giver: " + gameModel.getGiver());

            if(FXMLController.fullscreen)
                stage.setFullScreen(true);

            new Thread() {
                public void run() {
                    for(int i=90; i>=0; i--) {
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
        else if(GuiAnchor.gameModel.getGameState() == GameState.Registration) {
            Parent root = null;
            try {
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFiles/idle.fxml"));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFiles/idle.fxml"));
                root = loader.load();
                GuiAnchor.cont = loader.getController();
                gameModel.updateObserver(cont);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root, FXMLController.resX, FXMLController.resY);
            stage.setScene(scene);
            if(FXMLController.fullscreen)
                stage.setFullScreen(true);
        }
        else
            System.out.println("GameState unknown for GUI");
    }

    @Override
    public void onNotifiyQandA() {
        String s = "Q. and A.:\n\n";
        for(int i=0; i < GuiAnchor.gameModel.getQAndA().size(); i++) {
            String[] QA = GuiAnchor.gameModel.getQAndA().get(i);
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
        for(String str : GuiAnchor.gameModel.getExplanations()) {
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
                for(Guess g : GuiAnchor.gameModel.getGuesses()) {
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
