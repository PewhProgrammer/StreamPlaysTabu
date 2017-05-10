package gui.webinterface.containers;


public class PrevotedCategoriesContainer {

    private final String cat1;
    private final String cat2;
    private final String cat3;
    private final String cat4;
    private final String cat5;

    public PrevotedCategoriesContainer(String[] categories) {
        cat1 = categories[0];
        cat2 = categories[1];
        cat3 = categories[2];
        cat4 = categories[3];
        cat5 = categories[4];
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
}
