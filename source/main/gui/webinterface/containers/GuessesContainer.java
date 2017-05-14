package gui.webinterface.containers;

import model.Guess;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class GuessesContainer {

    private String guess1 = "";
    private String guess2 = "";
    private String guess3 = "";
    private String guess4 = "";
    private String guess5 = "";
    private String guess6 = "";
    private String guess7 = "";
    private String guess8 = "";

    private double nr1 = 0;
    private double nr2 = 0;
    private double nr3 = 0;
    private double nr4 = 0;
    private double nr5 = 0;
    private double nr6 = 0;
    private double nr7 = 0;
    private double nr8 = 0;

    public GuessesContainer(LinkedList<Guess> guesses) {
        Iterator<Guess> it = guesses.iterator();
        Guess g;

        if (it.hasNext()) {
            g = it.next();
            guess1 = g.getGuess();
            nr1 = g.getScore();
        }

        if (it.hasNext()) {
            g = it.next();
            guess2 = g.getGuess();
            nr2 = g.getScore() / nr1;
        }

        if (it.hasNext()) {
            g = it.next();
            guess3 = g.getGuess();
            nr3 = g.getScore() / nr1;
        }

        if (it.hasNext()) {
            g = it.next();
            guess4 = g.getGuess();
            nr4 = g.getScore() / nr1;
        }

        if (it.hasNext()) {
            g = it.next();
            guess5 = g.getGuess();
            nr5 = g.getScore() / nr1;
        }

        if (it.hasNext()) {
            g = it.next();
            guess6 = g.getGuess();
            nr6 = g.getScore() / nr1;
        }

        if (it.hasNext()) {
            g = it.next();
            guess7 = g.getGuess();
            nr7 = g.getScore() / nr1;
        }

        if (it.hasNext()) {
            g = it.next();
            guess8 = g.getGuess();
            nr8 = g.getScore() / nr1;
        }

        nr1 = 1.0;
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


    public double getNr1() {
        return nr1;
    }

    public double getNr2() {
        return nr2;
    }

    public double getNr3() {
        return nr3;
    }

    public double getNr4() {
        return nr4;
    }

    public double getNr5() {
        return nr5;
    }

    public double getNr6() {
        return nr6;
    }

    public double getNr7() {
        return nr7;
    }

    public double getNr8() {
        return nr8;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("guess1", guess1);
        obj.put("nr1", nr1);
        obj.put("guess2", guess2);
        obj.put("nr2", nr2);
        obj.put("guess3", guess3);
        obj.put("nr3", nr3);
        obj.put("guess4", guess4);
        obj.put("nr4", nr4);
        obj.put("guess5", guess5);
        obj.put("nr5", nr5);
        obj.put("guess6", guess6);
        obj.put("nr6", nr6);
        obj.put("guess7", guess7);
        obj.put("nr7", nr7);
        obj.put("guess8", guess8);
        obj.put("nr8", nr8);

        return obj;
    }
}
