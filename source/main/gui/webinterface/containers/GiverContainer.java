package gui.webinterface.containers;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class GiverContainer {

    private final String name;
    private final int points;
    private final int level;

    public GiverContainer(String name, int points, int lvl) {
        this.name = name;
        this.points = points;
        this.level = lvl;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getLevel() {
        return level;
    }
}
