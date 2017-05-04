package gui.webinterface.containers;

import model.PrevoteCategory;

import java.util.ArrayList;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class PrevoteCategoryContainer {

    private final String first;
    private final String second;
    private final String third;
    private final String fourth;
    private final String fifth;
    private final String sixth;
    private final String seventh;
    private final String eighth;
    private final String ninth;
    private final String tenth;

    public PrevoteCategoryContainer(ArrayList<PrevoteCategory> categories) {
        first = categories.get(0).getCategory();
        second = categories.get(1).getCategory();
        third = categories.get(2).getCategory();
        fourth = categories.get(3).getCategory();
        fifth = categories.get(4).getCategory();
        sixth = categories.get(5).getCategory();
        seventh = categories.get(6).getCategory();
        eighth = categories.get(7).getCategory();
        ninth = categories.get(8).getCategory();
        tenth = categories.get(9).getCategory();
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public String getThird() {
        return third;
    }

    public String getFourth() {
        return fourth;
    }

    public String getFifth() {
        return fifth;
    }

    public String getSixth() {
        return sixth;
    }

    public String getSeventh() {
        return seventh;
    }

    public String getEighth() {
        return eighth;
    }

    public String getNinth() {
        return ninth;
    }

    public String getTenth() {
        return tenth;
    }
}
