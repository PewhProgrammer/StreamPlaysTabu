package logic.commands;

import model.GameModel;

/**
 * Created by Marc on 04.04.2017.
 */
public class Answer extends Command {

    public Answer(GameModel gm, String ch) {
        super(gm, ch);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        //Anti cheating mechanism
        return false;
    }
}
