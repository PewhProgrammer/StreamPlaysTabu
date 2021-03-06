package logic.commands;

import common.Log;
import model.GameModel;
import model.GameState;

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
    private final int valLevel;

    public Validate(GameModel gm, String ch, int ID, int valScore, String sender) {
        super(gm, ch);
        this.ID = ID;
        word = null;
        this.score = valScore;
        this.sender = sender;

        valLevel = gm.getValidationLevel();

        reference = gameModel.getValidationKey();
        tabooWords = gameModel.getValidationObjects();
    }

    public Validate(GameModel gm, String ch, String word, int valScore, String sender) {
        super(gm, ch);
        this.word = word;
        this.score = valScore;
        this.sender = sender;
        this.ID = -1;

        valLevel = gm.getValidationLevel();

        reference = gameModel.getValidationKey();
        tabooWords = gameModel.getValidationObjects();
    }

    @Override
    public void execute() {
        if(valLevel == 0){
            gameModel.getNeo4jWrapper().validateNode(word,score * 2 - 6);
        } else if(valLevel == 1){
            gameModel.getNeo4jWrapper().validateConnectionTaboo(reference, word, score * 2 - 6);
        } else {
            gameModel.getNeo4jWrapper().validateConnectionCategory(reference, word, score * 2 - 6);
        }
    }

    @Override
    public boolean validate() {

        //wrong time
        if(gameModel.getGameState() != GameState.Registration && gameModel.getGameState() != GameState.WaitingForGiver) {
            return false;
        }

        //wrong person
        if (!gameModel.contribute(sender, thisChannel)) {
            return false;
        }

        //wrong ID
        if ((ID < 1 || ID > 5) && word == null) {
            return false;
        }

        //wrong word
        if (word != null && !gameModel.getTabooWords().contains(word)) {
            return false;
        }

        //wrong score
        return !(score < 1 || score > 5);
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
