package model;

import common.Util;

import java.util.Date;

/**
 * Created by Lenovo on 18.04.2017.
 */
public class Guess implements Comparable<Guess>{

    private final static int BASE_INCREMENT = 100;
    private final static int DECREASE = 5;
    private final static double DECREASE_FREQUENCY = 5.0;

    private int score = 100;
    private int occurences = 1;
    private String guess;
    private Date lastDecrease;

    public Guess(String guess) {
        this.guess = guess;
        lastDecrease = new Date();
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

    public void decreaseScore() {

        Date newStamp = new Date();
        int oldScore = score;

        double d = Util.diffTimeStamp(lastDecrease, newStamp);
        while (d > DECREASE_FREQUENCY && score > 0) {
            score = Integer.max(0, score - DECREASE);
            d =- DECREASE_FREQUENCY;
        }

        if (score < oldScore) {
            lastDecrease = newStamp;
        }
    }

    public int getOccurences() {
        return occurences;
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
