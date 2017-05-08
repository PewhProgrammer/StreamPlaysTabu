var timeLeft = 30;
var categoryTopline = "";
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
        timeLeft = 20;
        twenties = true;
    }
}, 1000);

function updateRanking(ranking) {
    var json = JSON.parse(ranking.body);

    document.getElementById("first").innerHTML = json.firstName +"<br>"+ json.firstPoints;
    document.getElementById("second").innerHTML = json.secondName +"<br>"+ json.secondPoints;
    document.getElementById("third").innerHTML = json.thirdName +"<br>"+ json.thirdPoints;
    document.getElementById("fourth").innerHTML = json.fourthName +"<br>"+ json.fourthPoints;
    document.getElementById("fifth").innerHTML = json.fifthName +"<br>"+ json.fifthPoints;
    document.getElementById("sixth").innerHTML = json.sixthName +"<br>"+ json.sixthPoints;
    document.getElementById("seventh").innerHTML = json.seventhName +"<br>"+ json.seventhPoints;
    document.getElementById("eighth").innerHTML = json.eighthName +"<br>"+ json.eighthPoints;
    document.getElementById("ninth").innerHTML = json.ninthName +"<br>"+ json.ninthPoints;
    document.getElementById("tenth").innerHTML = json.tenthName +"<br>"+ json.tenthPoints;
}

function updateCategoryVote(categories) {
    var json = JSON.parse(categories.body);

    if(categoryTopline ==  "") {
        categoryTopline = document.getElementById("categoryText").innerHTML;
    }

    document.getElementById("categoryText").innerHTML =
        categoryTopline + "\n\n"
        + "1. " + json.first + "\t" + "2. " + json.second
        + "3. " + json.third + "\t" + "4. " + json.fourth
        + "5. " + json.fifth + "\t" + "6. " + json.sixth
        + "7. " + json.seventh + "\t" + "8. " + json.eighth
        + "9. " + json.ninth + "\t" + "10. " + json.tenth;
}

function updateValidation(validation) {
    console.log(validation);
}

function prepareRegister() {
    requestGameMode();
    stompClient.send(
        "/localJava/reqRegisterInfo",
        {},
        JSON.stringify({})
    );
}