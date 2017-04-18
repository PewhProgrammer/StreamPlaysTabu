package logic.commands;

import model.GameModel;

/**
 * Created by Marc on 04.04.2017.
 */
public class StreamerExplains extends Command {

    private String user;

    public StreamerExplains(GameModel gm, String ch, String user) {
        super(gm, ch);
        this.user = user;
    }

    @Override
    public void execute() {
        gameModel.setGameMode();
    }

    @Override
    public boolean validate() {
        return user.equals(thisChannel);
    }
}
