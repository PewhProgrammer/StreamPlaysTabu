package gui.webinterface.containers;


import org.json.JSONObject;

public class PrevotedCategoriesContainer {

    private String cat1;
    private String cat2;
    private String cat3;
    private String cat4;
    private String cat5;

    public PrevotedCategoriesContainer(String[] categories) {
        cat1 = categories[0];
        if (cat1 == null) {
            cat1 = "No category returned from DB";
        }
        cat2 = categories[1];
        if (cat2 == null) {
            cat2 = "No category returned from DB";
        }
        cat3 = categories[2];
        if (cat3 == null) {
            cat3 = "No category returned from DB";
        }
        cat4 = categories[3];
        if (cat4 == null) {
            cat4 = "No category returned from DB";
        }
        cat5 = categories[4];
        if (cat5 == null) {
            cat5 = "No category returned from DB";
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

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("cat1", cat1);
        obj.put("cat2", cat2);
        obj.put("cat3", cat3);
        obj.put("cat4", cat4);
        obj.put("cat5", cat5);

        return obj;
    }
}
