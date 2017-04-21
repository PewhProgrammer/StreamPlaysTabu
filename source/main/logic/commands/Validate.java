package logic.commands;

import model.GameModel;

/**
 * Created by Marc on 03.04.2017.
 */
public class Validate extends Command {

    private int ID, score;

    public Validate(GameModel gm, String ch, int ID, int valScore) {
        super(gm, ch);
        this.ID = ID;
        this.score = valScore;
    }

    @Override
    public void execute() {
        //TODO update score of validated information, remove from db if score is baaaaaaaad
    }

    @Override
    public boolean validate() {
        return false;
        //anything to do here?
    }

    public int getID() {
        return ID;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()) {
            return false;
        }

        Validate v = (Validate) o;
        return v.getID() == getID() && v.getScore() == getScore();
    }


}
