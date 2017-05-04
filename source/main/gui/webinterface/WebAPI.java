package gui.webinterface;

import gui.GuiAnchor;
import gui.webinterface.containers.GameModeContainer;
import gui.webinterface.containers.GameStateContainer;
import logic.GameControl;
import logic.commands.Setup;
import model.GameModel;
import model.IObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class WebAPI implements IObserver {

    public WebAPI() {
        GameControl.mModel.addObserver(this);
    }

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/startGame")
    public void startGame(SetupInformation si) {
            (new Setup(si, GameControl.mModel, si.getChannel())).execute();
    }

    public void send(String path, Object o) {
        this.template.convertAndSend("/localJS" + path, o);
    }

    public void onNotifyGameState() {
        System.out.println("GameState changed.");
        send("/GameState", new GameStateContainer(GameControl.mModel.getGameState()));
    }

    public void onNotifiyQandA() {
        System.out.println("Received Q&A.");
    }

    public void onNotifyCategoryChosen() {
        System.out.println("Category chosen.");
    }

    public void onNotifyExplanation() {
        System.out.println("Explanation!");
    }

    public void onNotifyWinner() {
        System.out.println("Winner!");
    }

    public void onNotifyGuess() {
        System.out.println("Guess!");
    }

    public void onNotifyScoreUpdate() {
        System.out.println("Score!");
    }

    public void onNotifyGameMode() {
        System.out.println("GameMode changed.");
        send("/GameMode", new GameModeContainer(GameControl.mModel.getGameMode()));
    }

    public void onNotifyKick() {
        System.out.println("Kickvote!");
    }

    public void onNotifyRegistrationTime() {
        System.out.println("RegistrationTime!");
    }
}
