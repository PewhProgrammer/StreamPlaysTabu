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

    private final int ID, score;
    private final String reference;
    private final String sender;
    private final Set<String> tabooWords;

    public Validate(GameModel gm, String ch, int ID, int valScore, String sender) {
        super(gm, ch);
        this.ID = ID;
        this.score = valScore;
        this.sender = sender;

        reference = gameModel.getExplainWord();
        tabooWords = gameModel.getTabooWords();
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

        if (!gameModel.contribute(sender, thisChannel)) {
            return false;
        }

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
