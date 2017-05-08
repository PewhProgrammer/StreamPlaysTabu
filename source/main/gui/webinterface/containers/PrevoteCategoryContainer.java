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

    public PrevoteCategoryContainer(String[] categories) {
        first = categories[0];
        second = categories[1];
        third = categories[2];
        fourth = categories[3];
        fifth = categories[4];
        sixth = categories[5];
        seventh = categories[6];
        eighth = categories[7];
        ninth = categories[8];
        tenth = categories[9];
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
