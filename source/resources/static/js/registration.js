var timeLeft = 30;
var twenties = false;
// Update the count down every 1 second
var x = setInterval(function() {

    timeLeft = timeLeft - 1;

    document.getElementById("countdown").innerHTML = timeLeft + "s";
    if(!twenties) {
        document.getElementById("progressbar").style.width = (timeLeft / 30) * 100 + "%";
    } else {
        document.getElementById("progressbar").style.width = (timeLeft / 20) * 100 + "%";
    }
    if (timeLeft == 0) {
        timeLeft = 30;
    }
}, 1000);

function updateRanking(ranking) {
    var json = JSON.parse(ranking.body);
    document.getElementById("first").innerHTML = json.firstName +"<br>"+ json.firstPoints + " Pts.";
    document.getElementById("second").innerHTML = json.secondName +"<br>"+ json.secondPoints + " Pts.";
    document.getElementById("third").innerHTML = json.thirdName +"<br>"+ json.thirdPoints + " Pts.";
    document.getElementById("fourth").innerHTML = json.fourthName +"<br>"+ json.fourthPoints + " Pts.";
    document.getElementById("fifth").innerHTML = json.fifthName +"<br>"+ json.fifthPoints + " Pts.";
    document.getElementById("sixth").innerHTML = json.sixthName +"<br>"+ json.sixthPoints + " Pts.";
    document.getElementById("seventh").innerHTML = json.seventhName +"<br>"+ json.seventhPoints + " Pts.";
    document.getElementById("eighth").innerHTML = json.eighthName +"<br>"+ json.eighthPoints + " Pts.";
    document.getElementById("ninth").innerHTML = json.ninthName +"<br>"+ json.ninthPoints + " Pts.";
    document.getElementById("tenth").innerHTML = json.tenthName +"<br>"+ json.tenthPoints + " Pts.";
}

function updateCategoryVote(categories) {
    var json = JSON.parse(categories.body);

    document.getElementById("categoryText").innerHTML =
        "1. " + json.first + getWhitespaces(10) + "2. " + json.second + "<br>"
        + "3. " + json.third + getWhitespaces(10) + "4. " + json.fourth + "<br>"
        + "5. " + json.fifth + getWhitespaces(10) + "6. " + json.sixth + "<br>"
        + "7. " + json.seventh + getWhitespaces(10) + "8. " + json.eighth + "<br>"
        + "9. " + json.ninth + getWhitespaces(10) + "10. " + json.tenth + "<br>";
}

function updateValidation(validation) {

    var json = JSON.parse(validation.body);
    var string = json.reference + "<br><small>";
    if (json.taboo1 != "") {
        string = string + "<ul><li>" + json.taboo1 + "</li>"
    }
    if (json.taboo2 != "") {
        string = string + "<li>" + json.taboo2 + "</li>"
    }
    if (json.taboo3 != "") {
        string = string + "<li>" + json.taboo2 + "</li>"
    }
    if (json.taboo4 != "") {
        string = string + "<li>" + json.taboo2 + "</li>"
    }
    if (json.taboo5 != "") {
        string = string + "<li>" + json.taboo2 + "</li></ul></small>"
    }

    document.getElementById("validateText").innerHTML = string;
}

function prepareRegister() {
    requestGameMode();
    stompClientLoc.send(
        "/localJava/reqRegisterInfo",
        {},
        JSON.stringify({})
    );
}

function getWhitespaces(num) {

    var whitespaces ="";
    for(var i=0;i<num;i++) {
        whitespaces = whitespaces + "&nbsp;";
    }

    return whitespaces;
}