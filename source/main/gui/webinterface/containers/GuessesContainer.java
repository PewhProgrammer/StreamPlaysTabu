package gui.webinterface.containers;

import model.Guess;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class GuessesContainer {

    private String guess1;
    private String guess2;
    private String guess3;
    private String guess4;
    private String guess5;
    private String guess6;
    private String guess7;
    private String guess8;
    private String guess9;
    private String guess10;

    public GuessesContainer(LinkedList<Guess> guesses) {
        Iterator<Guess> it = guesses.iterator();

        if (it.hasNext()) {
            guess1 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess2 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess3 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess4 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess5 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess6 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess7 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess8 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess9 = it.next().getGuess();
        }

        if (it.hasNext()) {
            guess10 = it.next().getGuess();
        }
    }

    public String getGuess1() {
        return guess1;
    }

    public String getGuess2() {
        return guess2;
    }

    public String getGuess3() {
        return guess3;
    }

    public String getGuess4() {
        return guess4;
    }

    public String getGuess5() {
        return guess5;
    }

    public String getGuess6() {
        return guess6;
    }

    public String getGuess7() {
        return guess7;
    }

    public String getGuess8() {
        return guess8;
    }

    public String getGuess9() {
        return guess9;
    }

    public String getGuess10() {
        return guess10;
    }
}
