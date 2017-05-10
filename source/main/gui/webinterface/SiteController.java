package gui.webinterface;

import gui.webinterface.containers.*;
import logic.GameControl;
import logic.commands.*;
import model.GameModel;
import model.GameState;
import model.IObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SiteController implements IObserver {

    private GameModel gm;

    public SiteController() {
        GameControl.mModel.addObserver(this);
        gm = GameControl.mModel;
        gm.setSiteController(this);
    }

    @Autowired
    private SimpMessagingTemplate template;

    public void send(String path, Object o) {
        this.template.convertAndSend("/externalJS" + path, o);
    }

    @MessageMapping("/giverJoined")
    public void giverJoined() {
        Command cmd = new GiverJoined(gm, "giver");
        gm.pushCommand(cmd);
    }

    @MessageMapping("/reqPrevotedCategories")
    public void reqPrevotedCategories() {
        send("/prevotedCategories", new PrevoteCategoryContainer(gm.getPrevotedCategories()));
    }

    @MessageMapping("/reqGiver")
    public void reqGiver() {
        String giver = gm.getGiver();
        int score = gm.getScore(giver);
        int lvl = gm.getLevel(score);
        send("/giver", new GiverContainer(giver, score, lvl));
    }

    @MessageMapping("/reqValidation")
    public void reqValidation() {
        //TODO: implement validation stuff
    }

    @MessageMapping("/reqSkip")
    public void reqSkip() {
        Command cmd = new Skip(gm, "giver");
        gm.pushCommand(cmd);
    }

    @MessageMapping("/sendCategory")
    public void receiveCategory(CategoryChosenContainer cg) {
        Command cmd = new CategoryChosen(gm, "giver", cg.getCategory());
        gm.pushCommand(cmd);
    }

    @MessageMapping("/sendExplanation")
    public void receiveExplanation(ExplanationContainer ec) {
        Command cmd = new Explanation(gm, "giver", ec.getExplanation(), ec.getGiver());
        gm.pushCommand(cmd);
    }

    @MessageMapping("/sendQandA")
    public void receiveQandA(QandAContainer qa) {
        Command cmd = new Answer(gm, "giver", qa.getQuestion(), qa.getAnswer());
        gm.pushCommand(cmd);
    }

    @MessageMapping("/sendValidation")
    public void receiveValidation() {
        //TODO: implement validation Logic
    }

    @Override
    public void onNotifyGameState() {
        if (gm.getGameState().equals(GameState.Win)) {
            send("/close", new GameCloseContainer("Win"));
        }

        if (gm.getGameState().equals(GameState.Lose)) {
            send("/close", new GameCloseContainer("Lose"));
        }
    }

    @Override
    public void onNotifyQandA() {
        //Nothing to do here
    }

    @Override
    public void onNotifyCategoryChosen() {
        //Nothing to do here
    }

    @Override
    public void onNotifyExplanation() {
        //Nothing to do here
    }

    @Override
    public void onNotifyWinner() {
        //Nothing to do here
    }

    @Override
    public void onNotifyGuess() {
        send("/guesses", new GuessesContainer(gm.getGuesses()));
    }

    @Override
    public void onNotifyScoreUpdate() {
        //Nothing to do here
    }

    @Override
    public void onNotifyGameMode() {
        //Nothing to do here
    }

    @Override
    public void onNotifyKick() {
        send("/close", new GameCloseContainer("Kick"));
    }

    @Override
    public void onNotifyRegistrationTime() {
        //Nothing to do here
    }

    @Override
    public void onNotifyExplainWord() {
        send("/explainWord", new ExplainWordContainer(gm.getExplainWord()));
    }

    @Override
    public void onNotifyTabooWords() {
        send("/tabooWords", new TabooWordsContainer(gm.getTabooWords()));
    }

    public void sendChatMessage(String time, String channel, String sender, String msg) {
        send("/chatMsg", new MessageContainer(time, channel, sender, msg));
    }

    public void sendQuestion(String question) {
        send("/question", new QuestionContainer(question));
    }
}
