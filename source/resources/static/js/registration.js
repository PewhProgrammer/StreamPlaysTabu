var timeLeft = 30;
var categoryTopline = "";
var validationTopline="";
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

    if(categoryTopline ==  "") {
        categoryTopline = document.getElementById("categoryText").innerHTML;
    }

    document.getElementById("categoryText").innerHTML =
        categoryTopline + "<br><br>"
        + "1. " + json.first + getWhitespaces(10) + "2. " + json.second + "<br>"
        + "3. " + json.third + getWhitespaces(10) + "4. " + json.fourth + "<br>"
        + "5. " + json.fifth + getWhitespaces(10) + "6. " + json.sixth + "<br>"
        + "7. " + json.seventh + getWhitespaces(10) + "8. " + json.eighth + "<br>"
        + "9. " + json.ninth + getWhitespaces(10) + "10. " + json.tenth + "<br>";
}

function updateValidation(validation) {
    if(validationTopline == "") {
        validationTopline = document.getElementById("validateText").innerHTML;
    }

    var json = JSON.parse(validation.body);
    document.getElementById("validateText").innerHTML =
        validationTopline + "<br>"
    + json.reference + "<br><small>"
    + "<ul><li>" + json.taboo1  + "</li>"
    + "<li>" + json.taboo2  + "</li>"
    + "<li>" + json.taboo3  + "</li>"
    + "<li>" + json.taboo4  + "</li>"
    + "<li>" + json.taboo5  + "</li></ul></small>"
}

function prepareRegister() {
    requestGameMode();
    stompClient.send(
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