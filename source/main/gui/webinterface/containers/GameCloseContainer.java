package gui.webinterface.containers;

import org.json.JSONObject;

public class GameCloseContainer {

    private final String status;
    private final String winner;
    private final String word;
    private final int points;

    public GameCloseContainer(String status, String winner, int points, String word) {
        this.status = status;
        this.winner = winner;
        this.points = points;
        this.word = word;
    }

    public String getStatus() {
        return status;
    }

    public String getWinner() {
        return winner;
    }

    public String getWord() { return word;}

    public int getPoints() {
        return points;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("status", status);
        obj.put("winner", winner);
        obj.put("points", points);
        obj.put("word", word);

        return obj;
    }
}
