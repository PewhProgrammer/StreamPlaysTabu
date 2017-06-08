package gui.webinterface;

import common.Log;
import common.database.Neo4jWrapper;
import gui.webinterface.containers.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import logic.GameControl;
import logic.commands.*;
import model.GameModel;
import model.GameState;
import model.IObserver;
import org.json.JSONObject;

import java.util.*;


public class SiteController implements IObserver {

    private static final String CORE_BASE = "/connection-server-core";

    private GameModel gm;
    private Socket socket;
    private int pw;

    public SiteController(GameModel model, String uri) throws Exception {
        this.gm = model;
        gm.addObserver(this);
        gm = GameControl.mModel;
        gm.setSiteController(this);

        socket = IO.socket(uri);
        initializeSocket();
    }

    private void initializeSocket() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Connected to the server!");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Disconnected");
            }
        }).on(CORE_BASE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject)args[0];
                System.out.println("Connected to server: " + obj.getString("time"));
            }
        }).on(CORE_BASE + "/giverJoined", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                giverJoined();
            }
        }).on(CORE_BASE + "/reqPrevotedCategories", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                reqPrevotedCategories();
            }
        }).on(CORE_BASE + "/reqGiver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                reqGiver();
            }
        }).on(CORE_BASE + "/reqValidation", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                reqValidation();
            }
        }).on(CORE_BASE + "/reqSkip", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                reqSkip();
            }
        }).on(CORE_BASE + "/sendCategory", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = new JSONObject((String)args[0]);
                if (validatePW(obj.getString("password"))) {
                    receiveCategory(new CategoryChosenContainer(obj.getString("category")));
                }
            }
        }).on(CORE_BASE + "/sendExplanation", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = new JSONObject((String)args[0]);
                if (validatePW(obj.getString("password"))) {
                    receiveExplanation(new ExplanationContainer(obj.getString("giver"), obj.getString("explanation")));
                }
            }
        }).on(CORE_BASE + "/sendQandA", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = new JSONObject((String)args[0]);
                if (validatePW(obj.getString("password"))) {
                    receiveQandA(new QandAContainer(obj.getString("q"), obj.getString("a")));
                }
            }
        }).on(CORE_BASE +"/sendValidation", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = new JSONObject((String)args[0]);
                if (validatePW(obj.getString("password"))) {
                    receiveValidation(obj.getString("reference"), obj.getString("taboo"), obj.getInt("score"));
                }
            }
        });
        socket.connect();
    }

    private boolean validatePW(String pw) {
        return(Integer.parseInt(pw) == this.pw);
    }

    public void send(String event, JSONObject o) {
        socket.emit(CORE_BASE + event, o.put("password", pw));
    }

    public void giverJoined() {
        System.out.println("Received GiverJoined.");
        Command cmd = new GiverJoined(gm, gm.getGiverChannel());
        gm.pushCommand(cmd);
    }

    public void reqPrevotedCategories() {
        System.out.println("Received reqPrevotedCategories.");
        send("/prevotedCategories", new PrevotedCategoriesContainer(gm.getPrevotedCategories()).toJSONObject());
    }

    public void reqGiver() {
        System.out.println("Received reqGiver.");
        String giver = gm.getGiver();
        int score = gm.getScore(giver,gm.getGiverChannel());
        int lvl = gm.getLevel(score);
        send("/giver", new GiverContainer(giver, score, lvl).toJSONObject());
    }

    public void reqValidation() {
        Log.info("Received Validation Request from Server");
        String[] references = new String[3];
        String[] taboos = new String[3];

        ArrayList<Neo4jWrapper.Pair>  k =
                gm.getNeo4jWrapper().getValidationForGiver();

        Collections.shuffle(k);
        int i = 0;
        for(Neo4jWrapper.Pair container : k){
            references[i] = container.getFirst().toString();
            taboos[i] = container.getSecond().toString();
            i++;
        }

        send("/validation", new GiverValidation(references, taboos).toJSONObject());
    }

    public void reqSkip() {
        System.out.println("Received reqSkip.");
        Command cmd = new Skip(gm, gm.getGiverChannel());
        gm.pushCommand(cmd);
    }

    public void receiveCategory(CategoryChosenContainer cg) {
        System.out.println("Received category: " + cg.getCategory());
        Command cmd = new CategoryChosen(gm, gm.getGiverChannel(), cg.getCategory());
        gm.pushCommand(cmd);
    }

    public void receiveExplanation(ExplanationContainer ec) {
        System.out.println("Received explanation: " + ec.getExplanation() + " by " + ec.getGiver());
        Command cmd = new Explanation(gm, gm.getGiverChannel(), ec.getExplanation(), ec.getGiver());
        gm.pushCommand(cmd);
    }

    public void receiveQandA(QandAContainer qa) {
        System.out.println("Received question: " + qa.getQuestion() + " and answer " + qa.getAnswer());
        Command cmd = new Answer(gm, gm.getGiverChannel(), qa.getQuestion(), qa.getAnswer());
        gm.pushCommand(cmd);
    }

    public void receiveValidation(String reference, String taboo, int score) {
        Log.info("Received sendValidation");
        //TODO decide which validation

        //gm.getNeo4jWrapper().validateConnection(reference, taboo, score * 2 - 4);
        //Give user 10 more seconds
        gm.setRoundTime(gm.getRoundTime() + 10);
        gm.notifyUpdateTime();
    }

    @Override
    public void onNotifyGameState() {
        if (gm.getGameState().equals(GameState.Win)) {
            send("/close", new GameCloseContainer("Win", gm.getWinner(), gm.getGainedPoints(), gm.getExplainWord()).toJSONObject());
        }

        if (gm.getGameState().equals(GameState.Lose)) {
            send("/close", new GameCloseContainer("Lose", "", 0, gm.getExplainWord()).toJSONObject());
        }

        if (gm.getGameState().equals(GameState.Kick)) {
            send("/close", new GameCloseContainer("Kick", "", 0, gm.getExplainWord()).toJSONObject());
        }

        send("/state", new GameStateContainer(gm.getGameState()).toJSONObject());
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
        send("/guesses", new GuessesContainer(gm.getGuesses()).toJSONObject());
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
       //nothing to do here
    }

    @Override
    public void onNotifyRegistrationTime() {
        //Nothing to do here
    }

    @Override
    public void onNotifyExplainWord() {
        send("/explainWord", new ExplainWordContainer(gm.getExplainWord()).toJSONObject());
    }

    @Override
    public void onNotifyTabooWords() {
        send("/tabooWords", new TabooWordsContainer(gm.getTabooWords()).toJSONObject());
    }

    @Override
    public void onNotifyUpdateTime() {

    }

    public void sendError(String msg) {
        send("/error", new ErrorContainer(msg).toJSONObject());
    }

    public void sendChatMessage(String channel, String sender, String msg) {
        send("/chatMessage", new MessageContainer(channel, sender, msg).toJSONObject());
    }

    public void sendQuestion(String question) {
        send("/question", new QuestionContainer(question).toJSONObject());
    }

    public int generatePW() {
        int i = this.pw;
        pw = 1000 + (int)(Math.random() * 8999);
        send("/updatePW", new JSONObject().put("old", i).put("new", pw));
        return pw;
    }
}
