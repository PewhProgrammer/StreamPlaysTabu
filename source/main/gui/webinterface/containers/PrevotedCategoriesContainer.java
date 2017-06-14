package gui.webinterface.containers;


import model.PrevoteCategory;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrevotedCategoriesContainer {

    private String cat1 = "";
    private String i1;
    private String cat2;
    private String i2;
    private String cat3;
    private String i3;
    private String cat4;
    private String i4;
    private String cat5;
    private String i5;

    public PrevotedCategoriesContainer(ArrayList<PrevoteCategory> categories) {
        if (categories.size() >= 5) {

            if (categories.get(0).getScore() == 0) {
                Collections.shuffle(categories);
            }

            cat1 = categories.get(0).getCategory();
            i1 = Integer.toString(categories.get(0).getScore());

            if (cat1 == null) {
                cat1 = "No category returned from DB";
            }
            if (i1.equals("0")) {
                i1 = "Random choice";
            }
            cat2 = categories.get(1).getCategory();
            i2 = Integer.toString(categories.get(1).getScore());
            if (cat2 == null) {
                cat2 = "No category returned from DB";
            }
            if (i2.equals("0")) {
                i2 = "Random choice";
            }
            cat3 = categories.get(2).getCategory();
            i3 = Integer.toString(categories.get(2).getScore());
            if (cat3 == null) {
                cat3 = "No category returned from DB";
            }
            if (i3.equals("0")) {
                i3 = "Random choice";
            }
            cat4 = categories.get(3).getCategory();
            i4 = Integer.toString(categories.get(3).getScore());
            if (cat4 == null) {
                cat4 = "No category returned from DB";
            }
            if (i4.equals("0")) {
                i4 = "Random choice";
            }
            cat5 = categories.get(4).getCategory();
            i5 = Integer.toString(categories.get(4).getScore());
            if (cat5 == null) {
                cat5 = "No category returned from DB";
            }
            if (i5.equals("0")) {
                i5 = "Random choice";
            }
        }
    }

    public String getCat1() {
        return cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public String getCat4() {
        return cat4;
    }

    public String getCat5() {
        return cat5;
    }

    public String getI1() {
        return i1;
    }

    public String getI2() {
        return i2;
    }

    public String getI3() {
        return i3;
    }

    public String getI4() {
        return i4;
    }

    public String getI5() {
        return i5;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("cat1", cat1);
        obj.put("cat2", cat2);
        obj.put("cat3", cat3);
        obj.put("cat4", cat4);
        obj.put("cat5", cat5);

        obj.put("i1", i1);
        obj.put("i2", i2);
        obj.put("i3", i3);
        obj.put("i4", i4);
        obj.put("i5", i5);

        return obj;
    }
}
