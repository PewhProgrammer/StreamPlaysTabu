package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Marc on 05.04.2017.
 */
public class Taboo extends Command {

    private final String suggestion;
    private final String sender;

    public Taboo(GameModel gm, String ch, String suggestion, String sender) {
        super(gm, ch);
        this.suggestion = suggestion;
        this.sender = sender;
    }

    @Override
    public void execute() {
        gameModel.tabooSuggetion(suggestion);
    }

    @Override
    public boolean validate() {
        if (!gameModel.contribute(sender, thisChannel)) {
            return false;
        }
        return gameModel.getGameState().equals(GameState.Registration);
    }

    public String getSuggestion() {
        return suggestion;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Taboo t = (Taboo) o;

        return suggestion.equals(t.getSuggestion()) && thisChannel.equals(t.getChannel());
    }
}
