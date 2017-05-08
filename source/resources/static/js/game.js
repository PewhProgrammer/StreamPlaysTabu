/**
 * Created by Marc on 02.05.2017.
 */
var guessesTopline = "";

var timeLeft = 90;
// Update the count down every 1 second
var timer = setInterval(function() {

    timeLeft = timeLeft - 1;

    document.getElementById("countdown").innerHTML = timeLeft + "s";
    document.getElementById("progressbar").style.width = (timeLeft / 90) * 100 + "%";

    if (timeLeft == 0) {
        document.getElementById("countdown").innerHTML = "Time's up!";
        clearInterval(timer);
    }
}, 1000);

function updateGiver(giver) {
    var json = JSON.parse(giver.body);

    document.getElementById("giverInfo").innerHTML = giver.name + "<br>" + giver.points + " Pts."
}

function updateGuesses(guesses) {
    var incJson = JSON.parse(guesses.body);
    document.getElementById("1stGuess").innerHTML = incJson.guess1;
    document.getElementById("2ndGuess").innerHTML = incJson.guess2;
    document.getElementById("3rdGuess").innerHTML = incJson.guess3;
    document.getElementById("4thGuess").innerHTML = incJson.guess4;
    document.getElementById("5thGuess").innerHTML = incJson.guess5;
    document.getElementById("6thGuess").innerHTML = incJson.guess6;
    document.getElementById("7thGuess").innerHTML = incJson.guess7;
}

function updateExplanations(explanations) {
    console.log(explanations);
    var incJson = JSON.parse(explanations.body);

    var string = "Explanations:<br>"
    for(var i=0;i<10;i++) {
        string = string + "- " + incJson[i] + "<br>";
    }

}

function updateQandA(qAndA) {
    var incJson = JSON.parse(qAndA.body);
    document.getElementById("qAndA").innerHTML = "Q: " + incJson.question + "<br><br>" + "A: " + incJson.answer;
}

function prepareGame() {
    requestGameMode();
    stompClient.send(
        "/localJava/reqGiverInfo",
        {},
        JSON.stringify({})
    );

    stompClient.send(
        "/localJava/reqCategory",
        {},
        JSON.stringify({})
    );
}

function categoryChosen(chosencategory) {
    console.log(chosencategory);
}