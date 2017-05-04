package gui.webinterface.containers;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class GiverContainer {

    private final String name;
    private final int points;

    public GiverContainer(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}
