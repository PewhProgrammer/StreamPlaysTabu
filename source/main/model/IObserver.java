package model;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public interface IObserver {

    //called when game state changes
    public void onNotifyGameState();

    //called when new question, answer pair is available
    public void onNotifiyQandA();

    //called when giver chose category
    public void onNotifyCategoryChosen();

    //called when giver gave new explanation
    public void onNotifyExplanation();

    //called if someone gave right answer
    public void onNotifyWinner();

    //called when guesses struct changes
    public void onNotifyGuess();

}
