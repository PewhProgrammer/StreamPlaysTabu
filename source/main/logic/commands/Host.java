package logic.commands;

import model.GameModel;

/**
 * Created by Marc on 05.04.2017.
 */
public class Host extends Command {

    private String host;

    public Host(GameModel gm, String ch, String host) {
        super(gm, ch);
        this.host = host;
    }

    @Override
    public void execute() {
        gameModel.host(host);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
