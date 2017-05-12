/**
 * Created by Marc on 02.05.2017.
 */

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

    document.getElementById("giverInfo").innerHTML = json.name + "<br>" + json.points + " Pts."
    document.getElementById("level").innerHTML = json.level;
}


function updateGuesses(guesses) {
    var incJson = JSON.parse(guesses.body);
    if(incJson.guess1 != "") {
        document.getElementById("1stGuess").innerHTML = incJson.guess1;
        document.getElementById("1stGuess").style.visibility = "visible";
        document.getElementById("Bar1").style.visibility = "visible";
        document.getElementById("1stGuessBar").style.width = parseFloat(incJson.nr1) * 100 + "%";
    }
    if(incJson.guess2 != "") {
        document.getElementById("2ndGuess").innerHTML = incJson.guess2;
        document.getElementById("2ndGuess").style.visibility = "visible";
        document.getElementById("Bar2").style.visibility = "visible";
        document.getElementById("2ndGuessBar").style.width = parseFloat(incJson.nr2) * 100 + "%";
    }
    if(incJson.guess3 != "") {
        document.getElementById("3rdGuess").innerHTML = incJson.guess3;
        document.getElementById("3rdGuess").style.visibility = "visible";
        document.getElementById("Bar3").style.visibility = "visible";
        document.getElementById("3rdGuessBar").style.width = parseFloat(incJson.nr3) * 100 + "%";
    }
    if(incJson.guess4 != "") {
        document.getElementById("4thGuess").innerHTML = incJson.guess4;
        document.getElementById("4thGuess").style.visibility = "visible";
        document.getElementById("Bar4").style.visibility = "visible";
        document.getElementById("4thGuessBar").style.width = parseFloat(incJson.nr4) * 100 + "%";
    }
    if(incJson.guess5 != "") {
        document.getElementById("5thGuess").innerHTML = incJson.guess5;
        document.getElementById("5thGuess").style.visibility = "visible";
        document.getElementById("Bar5").style.visibility = "visible";
        document.getElementById("5thGuessBar").style.width = parseFloat(incJson.nr5) * 100 + "%";
    }
    if(incJson.guess6 != "") {
        document.getElementById("6thGuess").innerHTML = incJson.guess6;
        document.getElementById("6thGuess").style.visibility = "visible";
        document.getElementById("Bar6").style.visibility = "visible";
        document.getElementById("6thGuessBar").style.width = parseFloat(incJson.nr6) * 100 + "%";
    }
    if(incJson.guess7 != "") {
        document.getElementById("7thGuess").innerHTML = incJson.guess7;
        document.getElementById("7thGuess").style.visibility = "visible";
        document.getElementById("Bar7").style.visibility = "visible";
        document.getElementById("7thGuessBar").style.width = parseFloat(incJson.nr7) * 100 + "%";
    }
}

function updateExplanations(explanations) {
    console.log(explanations);
    var incJson = JSON.parse(explanations.body);

    var string = "Explanations:<br><br>";
    if(incJson.exp1 != "") {
       string = string +  "<ul><li>"+ incJson.exp1 +"</li>";
    }
    if(incJson.exp2 != "") {
        string = string +  "<li>"+ incJson.exp2 +"</li>";
    }
    if(incJson.exp3 != "") {
        string = string +  "<li>"+ incJson.exp3 +"</li>";
    }
    if(incJson.exp4 != "") {
        string = string +  "<li>"+ incJson.exp4 +"</li>";
    }
    if(incJson.exp5 != "") {
        string = string +  "<li>"+ incJson.exp5 +"</li>";
    }
    if(incJson.exp6 != "") {
        string = string +  "<li>"+ incJson.exp6 +"</li>";
    }
    if(incJson.exp7 != "") {
        string = string +  "<li>"+ incJson.exp7 +"</li>";
    }
    if(incJson.exp8 != "") {
        string = string +  "<li>"+ incJson.exp8 +"</li>";
    }
    if(incJson.exp9 != "") {
        string = string +  "<li>"+ incJson.exp9 +"</li>";
    }
    if(incJson.exp10 != "") {
        string = string +  "<li>"+ incJson.exp10 +"</li></ul>";
    }

    document.getElementById("explanations").innerHTML = string;



}

function updateQandA(qAndA) {
    var incJson = JSON.parse(qAndA.body);
    document.getElementById("qAndA").innerHTML = "Q: " + incJson.question + "<br><br>" + "A: " + incJson.answer;
}

function prepareGame() {
    requestGameMode();
    stompClientLoc.send(
        "/localJava/reqGiverInfo",
        {},
        JSON.stringify({})
    );

    stompClientLoc.send(
        "/localJava/reqCategory",
        {},
        JSON.stringify({})
    );
}

function categoryChosen(chosencategory) {
    console.log(chosencategory);
}