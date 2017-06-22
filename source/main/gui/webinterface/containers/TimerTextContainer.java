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

    public TimerTextContainer(String s,String t,String b) {
        text = s;
        time = t;
        bonus = b;
    }

    public String getText() { return text; }

    public String getTime() { return time; }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();
        obj.put("text", text);
        if(time.length() > 0)
            obj.put("time", time);
        if(bonus.length() > 0)
            obj.put("bonus", bonus);

        return obj;
    }
}
