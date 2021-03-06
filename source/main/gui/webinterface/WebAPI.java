package gui.webinterface;

import common.Log;
import common.database.Neo4jWrapper;
import gui.webinterface.containers.*;
import logic.GameControl;
import model.Guess;
import logic.commands.Setup;
import model.GameMode;
import model.GameModel;
import model.GameState;
import model.IObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;


@Controller
public class WebAPI implements IObserver {

    private String channel = "";
    private int validateRotation = 0;
    private Neo4jWrapper db;

    private String noNeed = "We will ask you later if there is a need to validate!";

    public WebAPI() {
        GameControl.mModel.addObserver(this);
    }

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/startGame")
    public void startGame(SetupInformationContainer si) {
        Setup sCmd = (new Setup(si, GameControl.mModel, si.getChannel().toLowerCase()));
        db = GameControl.mModel.getNeo4jWrapper();
        if (sCmd.validate()) {
            channel = si.getChannel();
            sCmd.execute();
        } else {
            send("/err", "Could not connect to channel '" + si.getChannel() + "'. Please retry with a valid channel on Twitch or Beam.");
        }
    }

    @MessageMapping("/reqGameMode")
    public void requestGameMode() {
        send("/gameMode", new GameModeContainer(GameControl.mModel.getGameMode()));
    }

    @MessageMapping("/reqRegisterInfo")
    public void requestRegisterInfo() {
        onNotifyScoreUpdate();

        GameModel.prevotingLock.lock();
        PrevoteCategoryContainer pcc = new PrevoteCategoryContainer(GameControl.mModel.getPrevoteCategories());
        GameModel.prevotingLock.unlock();

        send("/prevoteCategory", pcc);

        int i = 0;

        GameControl.mModel.setValidationLevel(validateRotation);
        while (i < 3) {
            if (validateRotation == 0) { //explain
                i++;
                LinkedList<String> explains = db.getExplainForValidation(5);
                if (explains.isEmpty()) {
                    validateRotation++;
                    if (i == 3) {
                        validateRotation = -1;
                        Set<String> set = new HashSet<>();
                        set.add("");
                        send("/validation", new ValidationContainer(noNeed, set, 3));
                    }
                } else {
                    i = 3;
                    Set<String> set = new HashSet<>();
                    set.addAll(explains);
                    send("/validation", new ValidationContainer("", set, 0));
                    GameControl.mModel.setValidationKey("");
                    GameControl.mModel.setValidationObjects(set);
                }
            }
            if (validateRotation == 1) { // explain - taboo
                i++;
                //Map m = GameControl.mModel.getNeo4jWrapper().getTabooWordsForValidation(null, 5);
                //Iterator<Map.Entry<String, Set<String>>> it = m.entrySet().iterator();
                //if (it.hasNext()) {
                    //Map.Entry<String, Set<String>> mE = it.next();

                    Neo4jWrapper.Pair explainCategory = db.getTabooExplainForValidation();
                    Set<String> set = new HashSet<>();
                    set.add((String) explainCategory.getFirst());
                    String first = ((String) explainCategory.getSecond());

                    if (first.equals("EMPTY")) {
                        validateRotation++;
                        if (i == 3) {
                            validateRotation = -1;
                            set = new HashSet<>();
                            set.clear();
                            set.add("");
                            send("/validation", new ValidationContainer(noNeed, set, 3));
                        }
                    } else {
                        i = 3;
                        send("/validation", new ValidationContainer(first,set, 1));
                        GameControl.mModel.setValidationKey(first);
                        GameControl.mModel.setValidationObjects(set);
                    }
                //}
            }
            if (validateRotation == 2) { // category - explain
                i++;
                Neo4jWrapper.Pair explainCategory = db.getExplainCategoryForValidation();
                Set<String> set = new HashSet<>();
                set.add((String) explainCategory.getFirst());
                String first = ((String) explainCategory.getSecond());
                if (first.equals("EMPTY")) {
                    if (i == 3) {
                        first = noNeed;
                        set.clear();
                        set.add("");
                        send("/validation", new ValidationContainer(first, set, 3));
                    } else validateRotation = 0;

                } else {
                    i = 3;
                    send("/validation", new ValidationContainer(first, set, 2));
                    GameControl.mModel.setValidationKey(first);
                    GameControl.mModel.setValidationObjects(set);
                    validateRotation = -1;
                }
            }
        }

        validateRotation++;
    }

    @MessageMapping("/reqGiverInfo")
    public void requestGiverInfo() {
        String giver = GameControl.mModel.getGiver();
        int score = GameControl.mModel.getNeo4jWrapper().getUserPoints(giver, GameControl.mModel.getGiverChannel());
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
        GameState state = GameControl.mModel.getGameState();
        Log.trace("GameState changed to " + state);

        String winner = GameControl.mModel.getWinner();
        int points = GameControl.mModel.getGainedPoints();
        String word = GameControl.mModel.getExplainWord();

        if (state.equals(GameState.Win)) {
            send("/endGame", new GameCloseContainer("Win", winner, points, word));
            return;
        }

        if (state.equals(GameState.Lose)) {
            send("/endGame", new GameCloseContainer("Lose", winner, points, word));
            return;
        }

        if (state.equals(GameState.Kick)) {
            send("/endGame", new GameCloseContainer("Kick", GameControl.mModel.getGiver(), points, word));
            return;
        }

        send("/gameState", new GameStateContainer(state));
    }

    public void onNotifyQandA() {
        if (GameControl.mModel.getQAndA().size() > 0) {
            Log.trace("Received Q&A.");
            String q = "", a = "";
            q = GameControl.mModel.getQAndA().getFirst()[0];
            a = GameControl.mModel.getQAndA().getFirst()[1];
            send("/qAndA", new QandAContainer(q, a));
        }
    }

    public void onNotifyCategoryChosen() {
        Log.trace("Category chosen.");
        send("/category", new CategoryChosenContainer(GameControl.mModel.getCategory()));
    }

    public void onNotifyExplanation() {
        send("/explanations", new ExplanationsContainer(GameControl.mModel.getExplanations()));
    }

    public void onNotifyWinner() {
        //nothing to do here.
    }

    public void onNotifyGuess() {
        Log.trace("Guess!");
        List<Guess> guesses = GameControl.mModel.getGuesses();
        if (!guesses.isEmpty()) {
            send("/guesses", new GuessesContainer(GameControl.mModel.getGuesses()));
        }
    }

    public void onNotifyScoreUpdate() {
        Log.trace("Score!");
        GameModel gm = GameControl.mModel;
        if (!gm.getHosts().isEmpty()) {
            LinkedList<Neo4jWrapper.StreamerHighscore> sh = gm.getNeo4jWrapper().getStreamHighScore();
            if (sh.size() > 2) {
                send("/gameMode", new GameModeContainer(GameMode.HOST));
                send("/score", new StreamRankingContainer(sh));
            }
        } else {
            send("/gameMode", new GameModeContainer(GameMode.Normal));
            send("/score", new RankingContainer(GameControl.mModel.getNeo4jWrapper().getHighScoreList(10, channel)));
        }
    }

    public void onNotifyGameMode() {
        Log.trace("GameMode changed.");
        send("/gameMode", new GameModeContainer(GameControl.mModel.getGameMode()));
    }

    public void onNotifyKick() {
        Log.trace("votekick!");
    }

    public void onNotifyRegistrationTime() {
        //Nothing to do here
    }

    public void onNotifyTimerText(String s) {
        //Log.info("New TimerText sent: " + s);
        send("/updateTimerText", new TimerTextContainer(s));
    }

    public void onNotifyTimerText(String s,String time,String bonus) {
        //Log.info("New TimerText sent: " + s);
        send("/updateTimerText", new TimerTextContainer(s,time,bonus));
    }

    @Override
    public void onNotifyTimeStamp(String s) {

    }

    @Override
    public void onNotifyExplainWord() {
        //Nothing to do here
    }

    @Override
    public void onNotifyTabooWords() {
        //Nothing to do here
    }

    @Override
    public void onNotifyUpdateTime() {
        Log.trace("Game time updated");
        send("/updateTime", new GameModeContainer(GameControl.mModel.getGameMode()));
    }
}
