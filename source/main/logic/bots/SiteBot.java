package logic.bots;

import logic.commands.GiverJoined;
import model.GameModel;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

/**
 * Created by Marc on 03.04.2017.
 */
public class SiteBot {

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    private GameModel gameModel ;

    public SiteBot() {
        //connect to our server for the external webpage.
        try {
            //sock = new Socket("", 1234);
            //in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setGameModel(GameModel model){
        this.gameModel = model;
    }

    public void onGiverJoined(){
        gameModel.getCommands().push(new GiverJoined(gameModel,""));
    }

    public void sendQuestion(String question) {
        //TODO send !ask question
    }

    public void sendWord(String word, Set<String> tabooWords) {
        //TODO send word2explain + taboowords for giver
    }

    public void kick() {
        //TODO tell giver that he has been kicked and remove him from page
    }

    public void finish() {
        //TODO tell giver that his word has been guessed right and remove him from page
    }

    //TODO method for starting Thread that listens to server
}
