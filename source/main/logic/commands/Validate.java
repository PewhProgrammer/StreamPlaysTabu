package logic.commands;

import common.Log;
import model.GameModel;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Marc on 03.04.2017.
 */
public class Validate extends Command {

    private final int score;
    private final int ID;
    private final String reference;
    private final String sender;
    private final String word;
    private final Set<String> tabooWords;

    public Validate(GameModel gm, String ch, int ID, int valScore, String sender) {
        super(gm, ch);
        this.ID = ID;
        word = null;
        this.score = valScore;
        this.sender = sender;

        reference = gameModel.getExplainWord();
        tabooWords = gameModel.getTabooWords();
    }

    public Validate(GameModel gm, String ch, String word, int valScore, String sender) {
        super(gm, ch);
        this.word = word;
        this.score = valScore;
        this.sender = sender;
        this.ID = -1;

        reference = gameModel.getExplainWord();
        tabooWords = gameModel.getTabooWords();
    }

    @Override
    public void execute() {
        Iterator<String> it = tabooWords.iterator();
        String s = "";
        for (int i = 0; i < ID; i++) {
            s = it.next();
            if (s.equals(word)) {
                break;
            }
        }

        Log.trace("Received Validation: " + reference);
        //TODO what kind of validation?
        //gameModel.getNeo4jWrapper().validateConnection(reference, s, score * 2 - 4);
    }

    @Override
    public boolean validate() {

        if (!gameModel.contribute(sender, thisChannel)) {
            return false;
        }

        if (ID < 1 || ID > 5) {
            if (word == null || !tabooWords.contains(word)) {
                return false;
            }
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
