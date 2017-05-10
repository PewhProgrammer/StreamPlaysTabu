package gui.webinterface.containers;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class QandAContainer {

    private String question;
    private String answer;

    public QandAContainer() {}

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

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
