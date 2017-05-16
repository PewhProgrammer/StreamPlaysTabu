package logic.commands;

import logic.GameControl;
import model.GameModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Marc on 03.04.2017.
 */
public class Validate extends Command {

    private int ID, score;
    private String reference;
    private Set<String> tabooWords;

    public Validate(GameModel gm, String ch, int ID, int valScore) {
        super(gm, ch);
        this.ID = ID;
        this.score = valScore;

        Map m = GameControl.mModel.getNeo4jWrapper().getTabooWordsForValidation(null, 5);
        Iterator<Map.Entry<String, Set<String>>> it = m.entrySet().iterator();
        Map.Entry<String, Set<String>> mE = it.next();

        reference = mE.getKey();
        tabooWords = mE.getValue();

    }

    @Override
    public void execute() {
        Iterator<String> it = tabooWords.iterator();
        String s = "";
        for (int i = 0; i < ID; i++) {
            s = it.next();
        }

        gameModel.getNeo4jWrapper().validateExplainAndTaboo(reference, s, score * 2 - 4);
    }

    @Override
    public boolean validate() {
        if (ID < 1 || ID > tabooWords.size()) {
            return false;
        }

        if (score < 1 || score > 5) {
            return false;
        }

        return true;
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
