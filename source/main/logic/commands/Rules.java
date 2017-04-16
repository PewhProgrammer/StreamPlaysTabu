package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Rules extends Command {

    Bot bot;

    public Rules(GameModel gm, Bot b, String ch) {
        super(gm, ch);
        bot = b;
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        return false;
        //TODO check #rules in last x seconds to avoid spam and to not violate the limit
    }
}
