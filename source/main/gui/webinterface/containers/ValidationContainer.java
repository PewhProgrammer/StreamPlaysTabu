package gui.webinterface.containers;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class ValidationContainer {

    String reference;
    String taboo1 = "tim";
    String taboo2 = "tim";
    String taboo3 = "tim";
    String taboo4 = "tim";
    String taboo5 = "tim";

    public ValidationContainer(String reference, Set<String> tabooWords) {
        this.reference = reference;
        Iterator<String> it = tabooWords.iterator();

        if (it.hasNext()) {
            taboo1 = it.next();
        }

        if (it.hasNext()) {
            taboo2 = it.next();
        }

        if (it.hasNext()) {
            taboo3 = it.next();
        }

        if (it.hasNext()) {
            taboo4 = it.next();
        }

        if (it.hasNext()) {
            taboo5 = it.next();
        }
    }

    public String getReference() {
        return reference;
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

    public String getTaboo4() {
        return taboo4;
    }

    public String getTaboo5() {
        return taboo5;
    }
}
