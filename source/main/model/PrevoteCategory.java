package model;

/**
 * Created by Lenovo on 18.04.2017.
 */
public class PrevoteCategory implements Comparable<PrevoteCategory> {

    private int score;
    private String category;

    public PrevoteCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return this.score;
    }

    public void increaseScore() {
        score++;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int compareTo(PrevoteCategory pc) {
        if (score < pc.getScore()) {
            return 1;
        }

        if (score > pc.getScore()) {
            return -1;
        }

        return 0;
    }
}