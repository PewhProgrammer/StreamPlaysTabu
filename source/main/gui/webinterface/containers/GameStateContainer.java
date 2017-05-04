package gui.webinterface.containers;

import model.GameState;

public class GameStateContainer {

    private final String gameState;

    public GameStateContainer(GameState gs) {

        switch (gs) {
            case WaitingForGiver: {
                gameState = "WaitingForGiver";
                break;
            }
            case GameStarted: {
                gameState = "GameStarted";
                break;
            }
            default:
                gameState = "Register";
        }
    }

    public String getGameState() {
        return gameState;
    }
}
