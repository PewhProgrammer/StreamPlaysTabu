package gui.webinterface;

import gui.GuiAnchor;
import gui.webinterface.containers.*;
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

    @MessageMapping("/reqGameMode")
    public void requestGameMode() {
        send("/gameMode", new GameModeContainer(GameControl.mModel.getGameMode()));
    }

    @MessageMapping("/reqRegisterInfo")
    public void requestRegisterInfo() {
        onNotifyScoreUpdate();
        send("/prevoteCategory", new PrevoteCategoryContainer(GameControl.mModel.getPrevotedCategories()));
        send("/validation", new ValidationContainer(GameControl.mModel.getExplainWord(), GameControl.mModel.getTabooWords()));
    }

    public void send(String path, Object o) {
        this.template.convertAndSend("/localJS" + path, o);
    }

    public void onNotifyGameState() {
        System.out.println("GameState changed.");
        send("/gameState", new GameStateContainer(GameControl.mModel.getGameState()));
    }

    public void onNotifyQandA() {
        System.out.println("Received Q&A.");
        String q = GameControl.mModel.getQAndA().getLast()[0];
        String a = GameControl.mModel.getQAndA().getLast()[1];
        send("/qAndA", new QandAContainer(q,a));
    }

    public void onNotifyCategoryChosen() {
        System.out.println("Category chosen.");
        //TODO implement
    }

    public void onNotifyExplanation() {
        send("/explanations", new ExplanationsContainer(GameControl.mModel.getExplanations()));
    }

    public void onNotifyWinner() {
        System.out.println("Winner!");
        //TODO implement
    }

    public void onNotifyGuess() {
        System.out.println("Guess!");
        send("/guesses", new GuessesContainer(GameControl.mModel.getGuesses()));
    }

    public void onNotifyScoreUpdate() {
        System.out.println("Score!");
        send("/score", new RankingContainer());
    }

    public void onNotifyGameMode() {
        System.out.println("GameMode changed.");
        send("/gameMode", new GameModeContainer(GameControl.mModel.getGameMode()));
    }

    public void onNotifyKick() {
        System.out.println("Kickvote!");
    }

    public void onNotifyRegistrationTime() {
        System.out.println("RegistrationTime!");
    }
}
