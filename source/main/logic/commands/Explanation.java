package logic.commands;

import model.GameModel;

/**
 * Created by Marc on 04.04.2017.
 */
public class Explanation extends Command {

    private String explanation;

    public Explanation(GameModel gm, String ch, String explanation) {
        super(gm, ch);
        this.explanation = explanation;
    }


    @Override
    public void execute() {
        gameModel.addExplanation(explanation);
    }

    @Override
    public boolean validate() {
        //TODO anti cheating meachanism(, game state?)
        return false;
    }
}
