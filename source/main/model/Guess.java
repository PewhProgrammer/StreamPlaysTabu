package model;

/**
 * Created by Lenovo on 18.04.2017.
 */
public class Guess implements Comparable<Guess>{

    private final static int BASE_INCREMENT = 100;

    private int score = 0;
    private int occurences = 1;
    private String guess;

    public Guess(String guess) {
        this.guess = guess;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGuess() {
        return this.guess;
    }

    public void occured() {
        occurences++;
    }

    public void increaseScore() {
        score += Integer.max(1, BASE_INCREMENT / occurences);
    }

    @Override
    public int compareTo(Guess g) {
        if (score == g.getScore()) {
            return 0;
        }

        if (score < g.getScore()) {
            return 1;
        }

        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (!getClass().equals(o.getClass())) {
            return false;
        }
        Guess g = (Guess) o;
        return guess.equals(g.getGuess());
    }
}
