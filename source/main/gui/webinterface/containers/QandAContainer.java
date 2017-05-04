package gui.webinterface.containers;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class QandAContainer {

    private final String question;
    private final String answer;

    public QandAContainer(String q, String a) {
        question = q;
        answer = a;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
