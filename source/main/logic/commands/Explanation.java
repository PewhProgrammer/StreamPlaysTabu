package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 04.04.2017.
 */
public class Explanation extends Command {

    private String explanation;
    private String user;

    public Explanation(GameModel gm, String ch, String explanation, String user) {
        super(gm, ch);
        this.explanation = explanation;
        this.user = user;
    }


    @Override
    public void execute() {
        gameModel.getBot().sendChatMessage("Explanation given: " + explanation);
        gameModel.addExplanation(explanation);
    }

    @Override
    public boolean validate() {

        if (!user.equals(gameModel.getGiver())) {
            return false;
        }

        return gameModel.getGameState().equals(GameState.GameStarted);
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Explanation e = (Explanation) o;

        return explanation.equals(e.getExplanation());
    }
}
