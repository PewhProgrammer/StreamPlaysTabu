package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 05.04.2017.
 */
public class Taboo extends Command {

    private String suggestion;

    public Taboo(GameModel gm, String ch, String suggestion) {
        super(gm, ch);
        this.suggestion = suggestion;
    }

    @Override
    public void execute() {
        gameModel.tabooSuggetion(suggestion);
    }

    @Override
    public boolean validate() {
        return gameModel.getGameState().equals(GameState.Registration);
    }
}
