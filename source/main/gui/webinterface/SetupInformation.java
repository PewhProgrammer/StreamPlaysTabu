package gui.webinterface;

public class SetupInformation {

    private String gameMode;
    private String platform;
    private String channel;

    public String getGameMode() {
        return gameMode;
    }

    public String getPlatform() {
        return platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
