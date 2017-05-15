package gui.webinterface;

import gui.GuiAnchor;
import gui.webinterface.containers.*;
import logic.GameControl;
import logic.commands.CategoryChosen;
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
    public void startGame(SetupInformationContainer si) {
            (new Setup(si, GameControl.mModel, si.getChannel())).execute();
    }

    @MessageMapping("/reqGameMode")
    public void requestGameMode() {
        send("/gameMode", new GameModeContainer(GameControl.mModel.getGameMode()));
    }

    @MessageMapping("/reqRegisterInfo")
    public void requestRegisterInfo() {
        onNotifyScoreUpdate();
        send("/prevoteCategory", new PrevoteCategoryContainer(GameControl.mModel.getPrevoteCategories()));
        send("/validation", new ValidationContainer(GameControl.mModel.getExplainWord(), GameControl.mModel.getTabooWords()));
    }

    @MessageMapping("/reqGiverInfo")
    public void requestGiverInfo() {
        String giver = GameControl.mModel.getGiver();
        int score = GameControl.mModel.getNeo4jWrapper().getUserPoints(giver,GameControl.mModel.getGuesserChannel());
        int lvl = GameControl.mModel.getLevel(score);
        send("/giver", new GiverContainer(giver, score, lvl));
    }

    @MessageMapping("/reqCategory")
    public void requestCategory() {
        onNotifyCategoryChosen();
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
        String q ="", a = "";
        if(GameControl.mModel.getQAndA().size() > 0) {
            q = GameControl.mModel.getQAndA().getFirst()[0];
            a = GameControl.mModel.getQAndA().getFirst()[1];
        }
        send("/qAndA", new QandAContainer(q,a));
    }

    public void onNotifyCategoryChosen() {
        System.out.println("Category chosen.");
        send("/category", new CategoryChosenContainer(GameControl.mModel.getCategory()));
    }

    public void onNotifyExplanation() {
        send("/explanations", new ExplanationsContainer(GameControl.mModel.getExplanations()));
    }

    public void onNotifyWinner() {
        //nothing to do here.
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
        //TODO finish game with proper message
        System.out.println("Kickvote!");
    }

    public void onNotifyRegistrationTime() {
        //Nothing to do here
    }

    @Override
    public void onNotifyExplainWord() {
        //Nothing to do here
    }

    @Override
    public void onNotifyTabooWords() {
        //Nothing to do here
    }
}
