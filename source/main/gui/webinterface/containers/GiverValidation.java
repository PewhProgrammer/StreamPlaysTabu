package gui.webinterface.containers;

import org.json.JSONObject;

public class GiverValidation {

    private final String reference1;
    private final String reference2;
    private final String reference3;

    private final String taboo1;
    private final String taboo2;
    private final String taboo3;

    public GiverValidation(String[] references, String[] taboos) {
        reference1 = references[0];
        reference2 = references[1];
        reference3 = references[2];

        taboo1 = taboos[0];
        taboo2 = taboos[1];
        taboo3 = taboos[2];
    }

    public String getReference1() {
        return reference1;
    }

    public String getReference2() {
        return reference2;
    }

    public String getReference3() {
        return reference3;
    }

    public String getTaboo1() {
        return taboo1;
    }

    public String getTaboo2() {
        return taboo2;
    }

    public String getTaboo3() {
        return taboo3;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();
        obj.put("reference1", reference1);
        obj.put("taboo1", taboo1);
        obj.put("reference2", reference2);
        obj.put("taboo2", taboo2);
        obj.put("reference3", reference3);
        obj.put("taboo3", taboo3);
        return obj;
    }
}
