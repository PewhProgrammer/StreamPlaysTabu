var timeLeft = 30;
// Update the count down every 1 second
var x = setInterval(function() {

    timeLeft = timeLeft - 1;

    document.getElementById("progressbar").innerHTML = timeLeft + "s";
    document.getElementById("progressbar").style.width = (timeLeft / 30) * 100 + "%";

    if (timeLeft == 0) {
        document.getElementById("statusInfo").innerHTML = "Time to register!";
        timeLeft = 30;
    }
}, 1000);

function updateRanking(ranking) {
    if (window.location.href == 'http://localhost:8080/registerFFA.html') {
        var json = JSON.parse(ranking.body);
        document.getElementById("first").innerHTML = json.firstName + "<br>" + json.firstPoints + " Pts.";
        document.getElementById("second").innerHTML = json.secondName + "<br>" + json.secondPoints + " Pts.";
        document.getElementById("third").innerHTML = json.thirdName + "<br>" + json.thirdPoints + " Pts.";
        document.getElementById("fourth").innerHTML = json.fourthName + "<br>" + json.fourthPoints + " Pts.";
        document.getElementById("fifth").innerHTML = json.fifthName + "<br>" + json.fifthPoints + " Pts.";
        document.getElementById("sixth").innerHTML = json.sixthName + "<br>" + json.sixthPoints + " Pts.";
        document.getElementById("seventh").innerHTML = json.seventhName + "<br>" + json.seventhPoints + " Pts.";
        document.getElementById("eighth").innerHTML = json.eighthName + "<br>" + json.eighthPoints + " Pts.";
        document.getElementById("ninth").innerHTML = json.ninthName + "<br>" + json.ninthPoints + " Pts.";
        document.getElementById("tenth").innerHTML = json.tenthName + "<br>" + json.tenthPoints + " Pts.";
    } else {
        //TODO write json content into StreamScore
    }
}

function updateCategoryVote(categories) {
    var json = JSON.parse(categories.body);
    var uneven= "";
    if(json.first != null) {
        uneven = uneven + "1. " + json.first + "<br>";
    }
    if(json.third != null) {
        uneven = uneven + "3. " + json.third + "<br>";
    }
    if(json.fifth != null) {
        uneven = uneven + "5. " + json.fifth + "<br>";
    }
    if(json.seventh != null) {
        uneven = uneven + "7. " + json.seventh + "<br>";
    }
    if(json.ninth != null) {
        uneven = uneven + "9. " + json.ninth;
    }
    document.getElementById("categoryText1").innerHTML = uneven;

    var even = "";
    if(json.second != null) {
        even = even + "2. " + json.second + "<br>";
    }
    if(json.fourth != null) {
        even = even + "4. " + json.fourth + "<br>";
    }
    if(json.sixth != null) {
        even = even + "6. " + json.sixth + "<br>";
    }
    if(json.eighth != null) {
        even = even + "8. " + json.eighth + "<br>";
    }
    if(json.tenth != null) {
        even = even + "10. " + json.tenth;
    }
    document.getElementById("categoryText2").innerHTML = even;
}

function updateValidation(validation) {

    var json = JSON.parse(validation.body);
    var string = "<big>" + json.reference + "</big>" + "<br><small>";
    if (json.taboo1 != "") {
        string = string + "<ul><li>" + json.taboo1 + "</li>"
    }
    if (json.taboo2 != "") {
        string = string + "<li>" + json.taboo2 + "</li>"
    }
    if (json.taboo3 != "") {
        string = string + "<li>" + json.taboo3 + "</li>"
    }
    if (json.taboo4 != "") {
        string = string + "<li>" + json.taboo4 + "</li>"
    }
    if (json.taboo5 != "") {
        string = string + "<li>" + json.taboo5 + "</li></ul></small>"
    }

    document.getElementById("validateText").innerHTML = string;
}

function prepareRegister() {
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

var  tips = ["Type !register to get into the giver pool! Being registered you have the chance to explain the next word!",
"You can type !score to see your points. The top 3 players with the most points will be granted a badge!",
"You can take a guess by using the !guess command. The faster you guess the right word, the more points you will get!",
"Do you want to ask the giver anything? Type !ask and the giver can answer your question.",
"Is a player not behaving right? Start a !votekick.",
"As a giver you can get extra time by validating some taboo words.",
"Try giving short and simple explanations! The faster someone guesses your word, the more points both of you will receive!",
"Some templates can only be used once per round! Be careful when choosing a template for explaining.",
"Always keep an eye on the chat and their guesses. It will help you to find the right explanations!"];

var  y = setInterval(function() {

    document.getElementById("hintText").innerHTML = "<big>Did you know?</big><br><small>"
    + tips[Math.round((Math.random() * 8))] + "</small>";

}, 10000);