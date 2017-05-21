package gui.webinterface;

import common.Log;
import gui.webinterface.containers.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jdk.nashorn.internal.parser.JSONParser;
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
                    Log.info("here");
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
        System.out.println("Received reqValidation.");
        String[] references = new String[3];
        String[] taboos = new String[3];

        ArrayList<ArrayList<String>>  k = GameControl.mModel.getNeo4jWrapper().getTabooWordsForValidationForGiver();

        int i = 0;
        for(ArrayList<String> container : k){
            references[i] = container.get(i);
            taboos[i] = container.get(i);
            i++;
        }

        /*
        Map m = GameControl.mModel.getNeo4jWrapper().getTabooWordsForValidation(null, 1);
        Iterator<Map.Entry<String, Set<String>>> it = m.entrySet().iterator();
        Map.Entry<String, Set<String>> mE = it.next();
        Iterator<String> itS = mE.getValue().iterator();

        references[0] = mE.getKey();
        taboos[0] = itS.next();

        m = GameControl.mModel.getNeo4jWrapper().getTabooWordsForValidation(null, 1);
        it = m.entrySet().iterator();
        mE = it.next();
        itS = mE.getValue().iterator();

        references[1] = mE.getKey();
        taboos[1] = itS.next();

        m = GameControl.mModel.getNeo4jWrapper().getTabooWordsForValidation(null, 1);
        it = m.entrySet().iterator();
        mE = it.next();
        itS = mE.getValue().iterator();

        references[2] = mE.getKey();
        taboos[2] = itS.next();*/


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
        System.out.println("Received sendValidation");
        gm.getNeo4jWrapper().validateExplainAndTaboo(reference, taboo, score * 2 - 4);
    }

    @Override
    public void onNotifyGameState() {
        if (gm.getGameState().equals(GameState.Win)) {
            send("/close", new GameCloseContainer("Win", gm.getWinner(), gm.getGainedPoints()).toJSONObject());
        }

        if (gm.getGameState().equals(GameState.Lose)) {
            send("/close", new GameCloseContainer("Lose", "", 0).toJSONObject());
        }

        if (gm.getGameState().equals(GameState.Kick)) {
            send("/close", new GameCloseContainer("Kick", "", 0).toJSONObject());
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
