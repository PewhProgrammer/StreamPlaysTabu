package logic.commands;

import model.GameModel;

/**
 * Created by Tim on 16.04.2017.
 */
public class Skip extends Command {

    private static final int SKIP_COST = 20;
    private String user;

    public Skip(GameModel gm, String ch, String user) {
        super(gm, ch);
        this.user = user;
    }

    public void execute() {
        gameModel.getSiteBot().sendWord(gameModel.generateExplainWord(), gameModel.generateTabooWords());
        gameModel.updateScore(user, SKIP_COST);
        gameModel.clearQAndA();
        gameModel.clearExplanations();
        gameModel.clearGuesses();
    }

    public boolean validate() {
     return true;
    }
}
