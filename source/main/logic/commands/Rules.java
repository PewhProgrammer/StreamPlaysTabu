package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Rules extends Command {

    private String user;

    public Rules(GameModel gm, String ch, String user) {
        super(gm, ch);
        this.user = user;
    }

    @Override
    public void execute() {
        gameModel.whisperRules(thisChannel, user);
    }

    @Override
    public boolean validate() {
        return true;
    }

    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Rules r = (Rules) o;

        return user.equals(r.getUser()) && thisChannel.equals(r.getChannel());
    }

    @Override
    public String toString(){
        return "Rules["+user+"]";
    }
}
