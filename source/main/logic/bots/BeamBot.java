package logic.bots;

import pro.beam.api.BeamAPI;

/**
 * Created by Marc on 03.04.2017.
 */
public class BeamBot extends Bot {

    BeamAPI api;

    public BeamBot() {
        api = new BeamAPI(""); //TODO get beamtoken and insert here!
    }

    @Override
    public void connectToChatroom(String user) {

    }

    @Override
    public void sendChatMessage(String msg) {

    }

    @Override
    public void whisperRules(String user) {

    }

    @Override
    public void whisperLink(String user, String link) {

    }

    @Override
    public void announceNewRound() {

    }

    @Override
    public void announceWinner() {

    }

    @Override
    public void announceRegistration() {

    }

}
