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
    private int validateRotation = 0 ;
    private Neo4jWrapper db;

    public WebAPI() {
        GameControl.mModel.addObserver(this);
    }

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/startGame")
    public void startGame(SetupInformationContainer si) {
        Setup sCmd = (new Setup(si, GameControl.mModel, si.getChannel()));
        db = GameControl.mModel.getNeo4jWrapper() ;
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

        GameControl.mModel.setValidationLevel(validateRotation);
        if(validateRotation == 0){ //explain

            LinkedList<String> explains = db.getExplainForValidation(5);
            if(explains.isEmpty()) validateRotation++;
            else {
                Set<String> set = new HashSet<>();
                set.addAll(explains);
                send("/validation", new ValidationContainer("", set, 0));
                GameControl.mModel.setValidationKey("");
                GameControl.mModel.setValidationObjects(set);
            }
        }
        if (validateRotation == 1){ // explain - taboo

            Map m = GameControl.mModel.getNeo4jWrapper().getTabooWordsForValidation(null, 5);
            Iterator<Map.Entry<String, Set<String>>> it = m.entrySet().iterator();
            if (it.hasNext()) {
                Map.Entry<String, Set<String>> mE = it.next();
                if(mE.getKey().equals("EMPTY")){
                    validateRotation++;
                } else {
                    send("/validation", new ValidationContainer(mE.getKey(), mE.getValue(), 1));
                    GameControl.mModel.setValidationKey(mE.getKey());
                    GameControl.mModel.setValidationObjects(mE.getValue());
                }
            }
        }
        if (validateRotation == 2){ // category - explain
            Neo4jWrapper.Pair explainCategory = db.getExplainCategoryForValidation();
            Set<String> set = new HashSet<>();
            set.add((String)explainCategory.getFirst());
            String first = ((String)explainCategory.getSecond());
            if(first.equals("EMPTY")){
                first = "We will ask you later if there is a need to validate!";
                set.clear();
                set.add("");
                send("/validation", new ValidationContainer(first, set, 3));

            }else {
                send("/validation", new ValidationContainer(first, set, 2));
                GameControl.mModel.setValidationKey((String) explainCategory.getSecond());
                GameControl.mModel.setValidationObjects(set);
            }
            validateRotation = -1;
        }
        validateRotation++;
    }

    @MessageMapping("/reqGiverInfo")
    public void requestGiverInfo() {
        String giver = GameControl.mModel.getGiver();
        int score = GameControl.mModel.getNeo4jWrapper().getUserPoints(giver,GameControl.mModel.getGiverChannel());
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
        System.out.println("GameState changed to " + state);

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
            send("/endGame", new GameCloseContainer("Kick", winner, points, word));
            return;
        }

        send("/gameState", new GameStateContainer(state));
    }

    public void onNotifyQandA() {
        if(GameControl.mModel.getQAndA().size() > 0) {
            System.out.println("Received Q&A.");
            String q ="", a = "";
            q = GameControl.mModel.getQAndA().getFirst()[0];
            a = GameControl.mModel.getQAndA().getFirst()[1];
            send("/qAndA", new QandAContainer(q,a));
        }
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
        List<Guess> guesses = GameControl.mModel.getGuesses();
        if (!guesses.isEmpty()) {
            send("/guesses", new GuessesContainer(GameControl.mModel.getGuesses()));
        }
    }

    public void onNotifyScoreUpdate() {
        System.out.println("Score!");
        GameModel gm = GameControl.mModel;
        if (!gm.getHosts().isEmpty()) {
            LinkedList<Neo4jWrapper.StreamerHighscore> sh = gm.getNeo4jWrapper().getStreamHighScore();
            if (sh.size() > 2) {
                send("/gameMode", new GameModeContainer(GameMode.HOST));
                send("/score", new StreamRankingContainer(sh));
            }
        }

        send("/gameMode", new GameModeContainer(GameMode.Normal));
        send("/score", new RankingContainer(GameControl.mModel.getNeo4jWrapper().getHighScoreList(10, channel)));
    }

    public void onNotifyGameMode() {
        System.out.println("GameMode changed.");
        send("/gameMode", new GameModeContainer(GameControl.mModel.getGameMode()));
    }

    public void onNotifyKick() {
        //nothing to do here
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

    @Override
    public void onNotifyUpdateTime(){
        Log.trace("Game time updated");
        send("/updateTime", new GameModeContainer(GameControl.mModel.getGameMode()));
    }
}
