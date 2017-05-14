package gui.webinterface.containers;


import org.json.JSONObject;

public class QuestionContainer {

    private final String question;

    public QuestionContainer(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("question", this.question);

        return obj;
    }
}
