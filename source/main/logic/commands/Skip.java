package logic.commands;

import model.GameModel;

/**
 * Created by Tim on 16.04.2017.
 */
public class Skip extends Command {

    public Skip(GameModel gm, String ch) {
        super(gm, ch);
    }

    public void execute() {
        //TODO send new explain+taboowords, decrease giver's score
    }

    public boolean validate() {
     return false;
     //anything to do here?
    }
}
