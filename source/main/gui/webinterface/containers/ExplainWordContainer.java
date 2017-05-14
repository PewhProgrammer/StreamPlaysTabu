package gui.webinterface.containers;


import org.json.JSONObject;

public class ExplainWordContainer {

    private final String explainWord;

    public ExplainWordContainer(String explainWord) {
        this.explainWord = explainWord;
    }

    public String getExplainWord() {
        return explainWord;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("explainWord", explainWord);

        return obj;
    }
}
