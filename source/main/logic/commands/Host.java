package logic.commands;

import model.GameModel;

/**
 * Created by Marc on 05.04.2017.
 */
public class Host extends Command {

    public Host(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {
        //check if stream is already host
        //if yes, remove him from host list and make bots disconnect from corresponding channel
        //if no, add him to host list and make bots connect to corresponding channel
    }

    @Override
    public boolean validate() {
        return false;
        //anything to do here?
    }
}
