package logic.commands;

import model.GameModel;

/**
 * Created by Tim on 16.04.2017.
 */
public class Skip extends Command {

    private static final int SKIP_COST = 20;

    public Skip(GameModel gm, String ch) {
        super(gm, ch);
    }

    public void execute() {
        gameModel.generateExplainWord();
        gameModel.generateTabooWords(getChannel());
        gameModel.updateScore(gameModel.getGiver(), SKIP_COST,getChannel());
        gameModel.clearQAndA();
        gameModel.clearExplanations();
        gameModel.clearGuesses();
    }

    public boolean validate() {
     return true;
    }

    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }
        return true;
    }
}
