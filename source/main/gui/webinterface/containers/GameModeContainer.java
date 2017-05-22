package gui.webinterface.containers;


import model.GameMode;

public class GameModeContainer {

    private String content;

    public GameModeContainer(GameMode gameMode) {
        this.content = gameMode == GameMode.HOST ? "SC" : "Free for all";
    }

    public String getContent() {
        return content;
    }
}
