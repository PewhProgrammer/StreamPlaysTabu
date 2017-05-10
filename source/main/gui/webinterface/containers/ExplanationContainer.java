package gui.webinterface.containers;

/**
 * Created by Lenovo on 10.05.2017.
 */
public class ExplanationContainer {

    private String giver;
    private String explanation;

    public ExplanationContainer() {}

    public ExplanationContainer(String giver, String explanation) {
        this.giver = giver;
        this.explanation = explanation;
    }

    public String getGiver() {
        return giver;
    }

    public void setGiver(String giver) {
        this.giver = giver;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
