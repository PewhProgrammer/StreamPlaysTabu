package gui.webinterface.containers;


import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

public class TabooWordsContainer {

    private String word1 = "";
    private String word2 = "";
    private String word3 = "";
    private String word4 = "";
    private String word5 = "";

    public TabooWordsContainer(Set<String> tabooWords) {
        Iterator<String> it = tabooWords.iterator();

        if (it.hasNext()) {
            word1 = it.next();
        }

        if (it.hasNext()) {
            word2 = it.next();
        }

        if (it.hasNext()) {
            word3 = it.next();
        }

        if (it.hasNext()) {
            word4 = it.next();
        }

        if (it.hasNext()) {
            word5 = it.next();
        }
    }

    public String getWord1() {
        return word1;
    }

    public String getWord2() {
        return word2;
    }

    public String getWord3() {
        return word3;
    }

    public String getWord4() {
        return word4;
    }

    public String getWord5() {
        return word5;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("word1", word1);
        obj.put("word2", word2);
        obj.put("word3", word3);
        obj.put("word4", word4);
        obj.put("word5", word5);

        return obj;
    }
}
