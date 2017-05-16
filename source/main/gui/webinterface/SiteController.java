package gui.webinterface;

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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class SiteController implements IObserver {

    private static final String CORE_BASE = "/connection-server-core";

    private GameModel gm;
    private Socket socket;

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
                receiveCategory(new CategoryChosenContainer(obj.getString("category")));
            }
        }).on(CORE_BASE + "/sendExplanation", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = new JSONObject((String)args[0]);
                receiveExplanation(new ExplanationContainer(obj.getString("giver"), obj.getString("explanation")));
            }
        }).on(CORE_BASE + "/sendQandA", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = new JSONObject((String)args[0]);
                receiveQandA(new QandAContainer(obj.getString("q"), obj.getString("a")));
            }
        }).on("/sendValidation", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = new JSONObject((String)args[0]);
                receiveValidation(obj.getString("reference"), obj.getString("taboo"), Integer.getInteger(obj.getString("score")));
            }
        });
        socket.connect();
    }

    public void send(String event, JSONObject o) {
        socket.emit(CORE_BASE + event, o);
    }

    public void giverJoined() {
        System.out.println("Received GiverJoined.");
        Command cmd = new GiverJoined(gm, "giver");
        gm.pushCommand(cmd);
    }

    public void reqPrevotedCategories() {
        System.out.println("Received reqPrevotedCategories.");
        send("/prevotedCategories", new PrevotedCategoriesContainer(gm.getPrevotedCategories()).toJSONObject());
    }

    public void reqGiver() {
        System.out.println("Received reqGiver.");
        String giver = gm.getGiver();
        int score = gm.getScore(giver,gm.getGuesserChannel());
        int lvl = gm.getLevel(score);
        send("/giver", new GiverContainer(giver, score, lvl).toJSONObject());
    }

    public void reqValidation() {
        System.out.println("Received reqValidation.");
        String[] references = new String[3];
        String[] taboos = new String[3];

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
        taboos[2] = itS.next();


        send("/validation", new GiverValidation(references, taboos).toJSONObject());
    }

    public void reqSkip() {
        System.out.println("Received reqSkip.");
        Command cmd = new Skip(gm, "giver");
        gm.pushCommand(cmd);
    }

    public void receiveCategory(CategoryChosenContainer cg) {
        System.out.println("Received category: " + cg.getCategory());
        Command cmd = new CategoryChosen(gm, "giver", cg.getCategory());
        gm.pushCommand(cmd);
    }

    public void receiveExplanation(ExplanationContainer ec) {
        System.out.println("Received explanation: " + ec.getExplanation() + " by " + ec.getGiver());
        Command cmd = new Explanation(gm, "giver", ec.getExplanation(), ec.getGiver());
        gm.pushCommand(cmd);
    }

    public void receiveQandA(QandAContainer qa) {
        System.out.println("Received question: " + qa.getQuestion() + " and answer " + qa.getAnswer());
        Command cmd = new Answer(gm, "giver", qa.getQuestion(), qa.getAnswer());
        gm.pushCommand(cmd);
    }

    public void receiveValidation(String reference, String taboo, int score) {
        System.out.println("Received sendValidation");
        gm.getNeo4jWrapper().validateExplainAndTaboo(reference, taboo, score * 2 - 4);
    }

    @Override
    public void onNotifyGameState() {
        if (gm.getGameState().equals(GameState.Win)) {
            send("/close", new GameCloseContainer("Win").toJSONObject());
        }

        if (gm.getGameState().equals(GameState.Lose)) {
            send("/close", new GameCloseContainer("Lose").toJSONObject());
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
        send("/close", new GameCloseContainer("Kick").toJSONObject());
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

    public void sendChatMessage(String time, String channel, String sender, String msg) {
        send("/chatMsg", new MessageContainer(time, channel, sender, msg).toJSONObject());
    }

    public void sendQuestion(String question) {
        send("/question", new QuestionContainer(question).toJSONObject());
    }
}
