package gui.webinterface.containers;


import org.json.JSONObject;

public class ErrorContainer {

    private final String msg;

    public ErrorContainer(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("msg", msg);
        return obj;
    }

}
