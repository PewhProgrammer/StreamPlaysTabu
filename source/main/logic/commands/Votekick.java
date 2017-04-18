package logic.commands;

import model.GameModel;
import model.GameState;

/**
 * Created by Tim on 18.04.2017.
 */
public class Votekick extends Command {

    private String votingUser;

    public Votekick(GameModel gm, String ch, String votingUser) {
        super(gm, ch);
    }

    @Override
    public void execute() {
        int numPlayers = gameModel.getNumPlayers();
        gameModel.getVotekick().add(votingUser);
        int numVotes = gameModel.getVotekick().size();

        if (numVotes > numPlayers * 0.5f) {
            gameModel.clear();
            gameModel.generateVotingCategories();
            gameModel.setGameState(GameState.Registration);
            gameModel.getSiteBot().kick();
            gameModel.notifyKick();
        }
    }

    @Override
    public boolean validate() {
        return !gameModel.getVotekick().contains(votingUser);
    }
}
