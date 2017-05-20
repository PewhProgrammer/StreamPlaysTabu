package logic.commands;

import model.GameModel;
import model.GameState;


public class Ask extends Command {

    private final String question;
    private final String sender;

    public Ask(GameModel gm, String ch, String sender, String question) {
        super(gm, ch);
        this.question = question;
        this.sender = sender;
    }

    /**
     * sends a question to the giver's page
     */
    @Override
    public void execute() {

        gameModel.getSiteController().sendQuestion(question);
    }

    @Override
    public boolean validate() {
        return gameModel.contribute(sender, thisChannel) && gameModel.getGameState().equals(GameState.GameStarted)
                && !getQuestion().equals("");
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Ask a = (Ask) o;

        return question.equals(a.getQuestion()) && thisChannel.equals(a.getChannel());
    }
}
