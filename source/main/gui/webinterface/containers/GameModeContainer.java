package gui.webinterface.containers;


import model.GameMode;

public class GameModeContainer {

    private String content;

    public GameModeContainer(GameMode gameMode) {
        this.content = gameMode == GameMode.Normal ? "Free for all" : "Streamer explains";
    }

    public String getContent() {
        return content;
    }
}
