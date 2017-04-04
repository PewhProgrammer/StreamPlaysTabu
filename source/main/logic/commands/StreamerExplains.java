package logic.commands;

import model.GameModel;

/**
 * Created by Marc on 04.04.2017.
 */
public class StreamerExplains extends Command {

    public StreamerExplains(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}
