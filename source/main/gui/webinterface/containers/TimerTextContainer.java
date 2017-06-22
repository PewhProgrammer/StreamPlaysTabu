package gui.webinterface.containers;

import org.json.JSONObject;

/**
 * Created by Marc on 20.06.2017.
 */
public class TimerTextContainer {

    private String text;
    private String time;
    private String bonus;

    public TimerTextContainer(String s) {
        text = s;
        time = "";
        bonus = "";
    }

    public TimerTextContainer(String s, String t, String b) {
        text = s;
        time = t;
        bonus = b;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public String getBonus() {
        return bonus;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();
        obj.put("text", text);
        obj.put("time", time);
        obj.put("bonus", bonus);

        return obj;
    }
}
