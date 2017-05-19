package gui.webinterface.containers;

import org.json.JSONObject;

public class GameCloseContainer {

    private final String status;
    private final String winner;
    private final int points;

    public GameCloseContainer(String status, String winner, int points) {
        this.status = status;
        this.winner = winner;
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public String getWinner() {
        return winner;
    }

    public int getPoints() {
        return points;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("status", status);
        obj.put("winner", winner);
        obj.put("points", points);

        return obj;
    }
}
