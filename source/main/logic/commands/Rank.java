package logic.commands;

import logic.bots.Bot;
import model.GameModel;

/**
 * Created by Marc on 04.04.2017.
 */
public class Rank extends Command {

    private String user;

    public Rank(GameModel gm, String ch, String user) {
        super(gm, ch);
        this.user = user;
    }

    @Override
    public void execute() {
        gameModel.getBot().announceScore(user, gameModel.getScore(user,getChannel()));
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

        Rank r = (Rank) o;

        return user.equals(r.getUser()) && thisChannel.equals(r.getChannel());
    }

    @Override
    public String toString(){
        return "Rank";
    }
}
