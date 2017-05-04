var timeLeft = 30;
var categoryTopline = "";

// Update the count down every 1 second
var x = setInterval(function() {

    timeLeft = timeLeft - 1;

    document.getElementById("countdown").innerHTML = timeLeft + "s";
    document.getElementById("progressbar").style.width = (timeLeft / 30) * 100 + "%";

    if (timeLeft == 0) {
        timeLeft = 20;
    }
}, 1000);

function updateRanking(ranking) {
    var json = JSON.parse(ranking.body);

    document.getElementById("first").innerHTML = json.firstName +"\n"+ json.firstPoints;
    document.getElementById("second").innerHTML = json.secondName +"\n"+ json.secondPoints;
    document.getElementById("third").innerHTML = json.thirdName +"\n"+ json.thirdPoints;
    document.getElementById("fourth").innerHTML = json.fourthName +"\n"+ json.fourthPoints;
    document.getElementById("fifth").innerHTML = json.fifthName +"\n"+ json.fifthPoints;
    document.getElementById("sixth").innerHTML = json.sixthName +"\n"+ json.sixthPoints;
    document.getElementById("seventh").innerHTML = json.seventhName +"\n"+ json.seventhPoints;
    document.getElementById("eighth").innerHTML = json.eighthName +"\n"+ json.eighthPoints;
    document.getElementById("ninth").innerHTML = json.ninthName +"\n"+ json.ninthPoints;
    document.getElementById("tenth").innerHTML = json.tenthName +"\n"+ json.tenthPoints;
}

function updateCategoryVote(categories) {
    var json = JSON.parse(categories.body);

    if(categoryTopline ==  "") {
        categoryTopline = document.getElementById("categoryText").innerHTML;
    }

    document.getElementById("categoryText").innerHTML =
        categoryTopline + "\n"
        + "1. " + json.first + "\t" + "2. " + json.second
        + "3. " + json.third + "\t" + "4. " + json.fourth
        + "5. " + json.fifth + "\t" + "6. " + json.sixth
        + "7. " + json.seventh + "\t" + "8. " + json.eighth
        + "9. " + json.ninth + "\t" + "10. " + json.tenth;
}