package logic.commands;

import model.GameModel;
import model.GameState;

import java.util.Set;


public class Votekick extends Command {

    private String votingUser;

    public Votekick(GameModel gm, String ch, String votingUser) {
        super(gm, ch);
        this.votingUser = votingUser;
    }

    @Override
    public void execute() {
        gameModel.getVotekick().add(votingUser);
        int numVotes = gameModel.getVotekick().size();

        Set<String> hosts = gameModel.getHosts();
        int p = 0;
        for (String host : hosts) {
            p += gameModel.getBot().getUsers(host).size();
        }
        p += gameModel.getBot().getUsers(gameModel.getGiverChannel()).size();

        if (numVotes > p * 0.5f) {
            gameModel.setGameOutcome(GameState.Kick.toString());
            gameModel.clear();
            gameModel.getNeo4jWrapper().increaseUserError(gameModel.getGiver(), thisChannel);
            gameModel.getNeo4jWrapper().increaseUserError(gameModel.getGiver(), thisChannel);
            gameModel.getNeo4jWrapper().updateUserVoteKicked(gameModel.getGiver(), thisChannel);
            gameModel.setGameState(GameState.Kick);
            gameModel.notifyKick();
        }
    }

    @Override
    public boolean validate() {
        if (!gameModel.contribute(votingUser, thisChannel)) {
            return false;
        }
        return !gameModel.getVotekick().contains(votingUser);
    }

    public String getVotingUser() {
        return votingUser;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Votekick v = (Votekick) o;
        return votingUser.equals(v.getVotingUser()) && thisChannel.equals(v.getChannel());
    }
}
