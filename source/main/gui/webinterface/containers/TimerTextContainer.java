package gui.webinterface.containers;

import org.json.JSONObject;

/**
 * Created by Marc on 20.06.2017.
 */
public class TimerTextContainer {

    private String text;

    public TimerTextContainer(String s) {
        text = s;
    }

    public String getText() { return text; }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("text", text);

        return obj;
    }
}
