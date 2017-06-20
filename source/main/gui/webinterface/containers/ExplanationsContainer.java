package gui.webinterface.containers;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class ExplanationsContainer {

    private String exp1 = "";
    private String exp2 = "";
    private String exp3 = "";
    private String exp4 = "";
    private String exp5 = "";
    private String exp6 = "";
    private String exp7 = "";
    private String exp8 = "";
    private String exp9 = "";
    private String exp10 = "";

    public ExplanationsContainer(List<String> explanations) {

        ListIterator<String> it = explanations.listIterator(explanations.size());

        if (it.hasPrevious()) {
            exp1 = it.previous();
        }
        if (it.hasPrevious()) {
            exp2 = it.previous();
        }
        if (it.hasPrevious()) {
            exp3 = it.previous();
        }
        if (it.hasPrevious()) {
            exp4 = it.previous();
        }
        if (it.hasPrevious()) {
            exp5 = it.previous();
        }
        if (it.hasPrevious()) {
            exp6 = it.previous();
        }
        if (it.hasPrevious()) {
            exp7 = it.previous();
        }
        if (it.hasPrevious()) {
            exp8 = it.previous();
        }
        if (it.hasPrevious()) {
            exp9 = it.previous();
        }
        if (it.hasPrevious()) {
            exp10 = it.previous();
        }
    }

    public String getExp1() {
        return exp1;
    }

    public String getExp2() {
        return exp2;
    }

    public String getExp3() {
        return exp3;
    }

    public String getExp4() {
        return exp4;
    }

    public String getExp5() {
        return exp5;
    }

    public String getExp6() {
        return exp6;
    }

    public String getExp7() {
        return exp7;
    }

    public String getExp8() {
        return exp8;
    }

    public String getExp9() {
        return exp9;
    }

    public String getExp10() {
        return exp10;
    }
}
