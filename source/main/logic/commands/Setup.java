package logic.commands;


import gui.webinterface.containers.SetupInformationContainer;
import logic.bots.AltTwitchBot;
import logic.bots.BeamBot;
import model.GameMode;
import model.GameModel;
import model.GameState;


public class Setup extends Command {

    private final String channel;
    private final String platform;
    private final String gameMode;

    public Setup(SetupInformationContainer si, GameModel gm, String channel) {
        super(gm, channel);
        this.channel = si.getChannel();
        this.platform = si.getPlatform();
        this.gameMode = si.getGameMode();
    }


    @Override
    public void execute() {
        GameMode gm = gameMode.equals("Free for all") ? GameMode.Normal : GameMode.Streamer;
        gameModel.setGameMode(gm);
        gameModel.setBot(platform, channel);
        gameModel.setGiverChannel(channel);
        gameModel.setGameState(GameState.Registration);
    }

    @Override
    public boolean validate() {
        if (platform.equals("Twitch")) {
            return AltTwitchBot.checkChannelExist(channel);
        } else {
            return BeamBot.checkChannelExists(channel);
        }
    }
}
