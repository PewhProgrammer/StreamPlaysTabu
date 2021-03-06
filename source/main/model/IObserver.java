package model;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public interface IObserver {

    //called when game state changes
    public void onNotifyGameState();

    //called when new question, answer pair is available
    public void onNotifyQandA();

    //called when giver chose category
    public void onNotifyCategoryChosen();

    //called when giver gave new explanation
    public void onNotifyExplanation();

    //called if someone gave right answer
    public void onNotifyWinner();

    //called when guesses struct changes
    public void onNotifyGuess();

    //called when a user's score changes
    public void onNotifyScoreUpdate();

    //called when gamemode changes
    public void onNotifyGameMode();

    //called when giver has been votekicked
    public void onNotifyKick();

    public void onNotifyRegistrationTime();

    public void onNotifyExplainWord();

    public void onNotifyTabooWords();

    public void onNotifyUpdateTime();

    public void onNotifyTimerText(String s);

    public void onNotifyTimerText(String s,String time,String bonus);

    public void onNotifyTimeStamp(String s);

}
