package gui.webinterface.containers;

import org.json.JSONObject;

public class GameCloseContainer {

    private final String status;

    public GameCloseContainer(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("status", status);

        return obj;
    }
}
