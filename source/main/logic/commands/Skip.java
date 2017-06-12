package logic.commands;

import model.GameModel;


public class Skip extends Command {

    private static final int SKIP_COST = -20;

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
        gameModel.addExplanation("The word to be explained is from the category " + gameModel.getCategory());
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
