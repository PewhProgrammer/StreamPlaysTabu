package gui.webinterface.containers;

import model.GameState;
import org.json.JSONObject;

public class GameStateContainer {

    private final String gameState;

    public GameStateContainer(GameState gs) {

        switch (gs) {
            case WaitingForGiver: {
                gameState = "Waiting For Giver";
                break;
            }
            case GameStarted: {
                gameState = "Game Started";
                break;
            }
            case Win: {
                gameState = "Win";
                break;
            }
            case Lose: {
                gameState = "Lose";
                break;
            }
            default:
                gameState = "Register";
        }
    }

    public String getGameState() {
        return gameState;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("state", gameState);

        return obj;

    }

}
