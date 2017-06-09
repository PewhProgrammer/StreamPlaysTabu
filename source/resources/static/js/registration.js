var timeLeft = 30;
// Update the count down every 1 second
var x = setInterval(function() {

       if(timeLeft != 0){
    timeLeft = timeLeft - 1;

    document.getElementById("progressbar").innerHTML = timeLeft + "s";
    document.getElementById("progressbar").style.width = (timeLeft / 30) * 100 + "%";
}

    if (timeLeft == 0) {
        document.getElementById("progressbar").innerHTML = "Waiting...";
        document.getElementById("progressbar").style.width = "100%";
        document.getElementById("statusInfo").innerHTML = "Next User to register will become the giver!";
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
        document.getElementByID("sndStream").innerHTML = json.stream2 + "<br>" + json.points2 + " Pts.";
        if (json.contributors2[0] != "")
            document.getElementByID("firstCont2").innerHTML = json.contributors2[0] + "<br>" + json.userPoints2[0] + " Pts.";

        if (json.contributors2[1] != "")
            document.getElementByID("sndCont2").innerHTML = json.contributors2[1] + "<br>" + json.userPoints2[1] + " Pts.";

        if (json.contributors2[2] != "")
            document.getElementByID("thirdCont2").innerHTML = json.contributors2[2] + "<br>" + json.userPoints2[2] + " Pts.";

        document.getElementByID("firstStream").innerHTML = json.stream1 + "<br>" + json.points1 + " Pts.";
        if (json.contributors1[0] != "")
            document.getElementByID("firstCont1").innerHTML = json.contributors1[0] + "<br>" + json.userPoints1[0] + " Pts.";

        if (json.contributors1[1] != "")
            document.getElementByID("sndCont1").innerHTML = json.contributors1[1] + "<br>" + json.userPoints1[1] + " Pts.";

        if (json.contributors1[2] != "")
            document.getElementByID("thirdCont1").innerHTML = json.contributors1[2] + "<br>" + json.userPoints1[2] + " Pts.";

        document.getElementByID("thirdStream").innerHTML = json.stream3 + "<br>" + json.points3 + " Pts.";
        if (json.contributors3[0] != "")
            document.getElementByID("firstCont3").innerHTML = json.contributors3[0] + "<br>" + json.userPoints3[0] + " Pts.";

        if (json.contributors3[1] != "")
            document.getElementByID("sndCont3").innerHTML = json.contributors3[1] + "<br>" + json.userPoints3[1] + " Pts.";

        if (json.contributors3[2] != "")
            document.getElementByID("thirdCont3").innerHTML = json.contributors3[2] + "<br>" + json.userPoints3[2] + " Pts.";
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
        string = string + "<ol><li>" + json.taboo1 + "</li>"
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
        string = string + "<li>" + json.taboo5 + "</li></ol></small>"
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