var timeLeft = 30;
// Update the count down every 1 second
var x = setInterval(function () {

    if (timeLeft != 0) {
        timeLeft = timeLeft - 1;

        document.getElementById("progressbar").innerHTML = timeLeft + "s";
        document.getElementById("progressbar").style.width = (timeLeft / 30) * 100 + "%";
    }

    if (timeLeft == 0) {
        document.getElementById("progressbar").innerHTML = "Waiting...";
        document.getElementById("progressbar").style.width = "100%";
        document.getElementById("statusInfo").innerHTML = "Next user to register will become the giver!";
    }
}, 1000);

function animateRank() {
    var aTime = 450;

        $('#two-columns li').fadeTo(aTime, 1, function () {
          $(this).delay(9000).fadeTo(aTime, 0);
          wait(100);
        });

   /*$('#first').fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#second').delay(200).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#third').delay(400).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#fourth').delay(600).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#fifth').delay(800).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#sixth').delay(200).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#seventh').delay(400).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#eighth').delay(600).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#ninth').delay(800).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });
    $('#tenth').delay(1000).fadeTo(aTime, 1, function () {
        $(this).delay(9000).fadeTo(aTime, 0);
    });*/
}
animateRank();
var a = setInterval(function () {
    animateRank();
}, 11000);

function wait(ms){
   var start = new Date().getTime();
   var end = start;
   while(end < start + ms) {
     end = new Date().getTime();
  }
}

function updateRanking(ranking) {
    var json = JSON.parse(ranking.body);
    if (window.location.href == 'http://localhost:8080/registerFFA.html') {
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
        document.getElementById("sndStream").innerHTML = json.stream2 + "<br>" + json.points2 + " Pts.";
        if ((json.contributors2[0] != "") && (json.contributors2[0] != null))
            document.getElementById("firstCont2").innerHTML = json.contributors2[0] + "<br>" + json.userPoints2[0] + " Pts.";

        if ((json.contributors2[1] != "") && (json.contributors2[1] != null))
            document.getElementById("sndCont2").innerHTML = json.contributors2[1] + "<br>" + json.userPoints2[1] + " Pts.";

        if ((json.contributors2[2] != "") && (json.contributors2[2] != null))
            document.getElementById("thirdCont2").innerHTML = json.contributors2[2] + "<br>" + json.userPoints2[2] + " Pts.";

        document.getElementById("firstStream").innerHTML = json.stream1 + "<br>" + json.points1 + " Pts.";
        if ((json.contributors1[0] != "") && (json.contributors1[0] != null))
            document.getElementById("firstCont1").innerHTML = json.contributors1[0] + "<br>" + json.userPoints1[0] + " Pts.";

        if ((json.contributors1[1] != "") && (json.contributors1[1] != null))
            document.getElementById("sndCont1").innerHTML = json.contributors1[1] + "<br>" + json.userPoints1[1] + " Pts.";

        if ((json.contributors1[2] != "") && (json.contributors1[2] != null))
            document.getElementById("thirdCont1").innerHTML = json.contributors1[2] + "<br>" + json.userPoints1[2] + " Pts.";

        document.getElementById("thirdStream").innerHTML = json.stream3 + "<br>" + json.points3 + " Pts.";
        if ((json.contributors3[0] != "") && (json.contributors3[0] != null))
            document.getElementById("firstCont3").innerHTML = json.contributors3[0] + "<br>" + json.userPoints3[0] + " Pts.";

        if ((json.contributors3[1] != "") && (json.contributors3[1] != null))
            document.getElementById("sndCont3").innerHTML = json.contributors3[1] + "<br>" + json.userPoints3[1] + " Pts.";

        if ((json.contributors3[2] != "") && (json.contributors3[2] != null))
            document.getElementById("thirdCont3").innerHTML = json.contributors3[2] + "<br>" + json.userPoints3[2] + " Pts.";
    }
}

function updateCategoryVote(categories) {
    var json = JSON.parse(categories.body);
    var uneven = "";
    if (json.first != null) {
        uneven = uneven + "1. " + json.first + "<br>";
    }
    if (json.third != null) {
        uneven = uneven + "3. " + json.third + "<br>";
    }
    if (json.fifth != null) {
        uneven = uneven + "5. " + json.fifth + "<br>";
    }
    if (json.seventh != null) {
        uneven = uneven + "7. " + json.seventh + "<br>";
    }
    if (json.ninth != null) {
        uneven = uneven + "9. " + json.ninth;
    }
    document.getElementById("categoryText1").innerHTML = uneven;

    var even = "";
    if (json.second != null) {
        even = even + "2. " + json.second + "<br>";
    }
    if (json.fourth != null) {
        even = even + "4. " + json.fourth + "<br>";
    }
    if (json.sixth != null) {
        even = even + "6. " + json.sixth + "<br>";
    }
    if (json.eighth != null) {
        even = even + "8. " + json.eighth + "<br>";
    }
    if (json.tenth != null) {
        even = even + "10. " + json.tenth;
    }
    document.getElementById("categoryText2").innerHTML = even;
}

function updateValidation(validation) {

    var json = JSON.parse(validation.body);
   //console.log("Validation container: " + JSON.stringify(json));
    var string = "<big>" + json.reference + "</big>" + "<br><small>";
    if (json.taboo1 != "") {
        string = string + "<ol style='text-align:left'><li>" + json.taboo1 + "</li>"
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

    var howToValidate = ' Type</small> <span style="color: #FFAF0A">!validate</span> &lt;ID&gt; &lt;1-5&gt; <small>(1 = bad, 5 = good):</small>'

    if (json.id == 0) {
        document.getElementById("validateDescription").innerHTML = "<small>How good can these words be explained?<br> " + howToValidate;
        document.getElementById("tabooText").innerHTML =
            '<small>Do you have an idea for a new word to explain? <br>Type </small><span style="color: #FFAF0A">!taboo</span> &lt;word&gt;<small> to share it with us.</small>';
    } else if (json.id == 1) {
        document.getElementById("validateDescription").innerHTML = "<small>How fitting are these taboo words?<br>" + howToValidate;
        document.getElementById("tabooText").innerHTML =
            '<small>Do you have an idea for a new taboo word? <br>Type</small>  <span style="color: #FFAF0A">!taboo</span> &lt;word&gt;<small> to share it with us.</small>';
    } else if (json.id == 2) {
        document.getElementById("validateDescription").innerHTML = "<small>How good does this word fit to its category?<br> " + howToValidate;
        document.getElementById("tabooText").innerHTML =
            '<small>Ideas for a new word to explain in this category? Type </small><span style="color: #FFAF0A">!taboo</span> &lt;word&gt;';
    } else if (json.id == 3) {
        document.getElementById("validateDescription").innerHTML = " ";
        document.getElementById("tabooText").innerHTML = '';
    }
    document.getElementById("validateText").innerHTML = string;
}

function prepareRegister() {
    stompClientLoc.send(
        "/localJava/reqRegisterInfo", {},
        JSON.stringify({})
    );
}

function getWhitespaces(num) {

    var whitespaces = "";
    for (var i = 0; i < num; i++) {
        whitespaces = whitespaces + "&nbsp;";
    }

    return whitespaces;
}

var tips = ["Type !register to get into the giver pool! Being registered you have the chance to explain the next word!",
"You can type !score to see your points.",
"You can take a guess by using the !guess command. The faster you guess the right word, the more points you will get!",
"Do you want to ask the giver anything? Type !ask and the giver can answer your question.",
"Is a player not behaving right? Start a !votekick.",
"As a giver you can get extra time by validating some taboo words.",
"Try giving short and simple explanations! The faster someone guesses your word, the more points both of you will receive!",
"Some templates can only be used once per round! Be careful when choosing a template for explaining.",
"Always keep an eye on the chat and their guesses. It will help you to find the right explanations!"];

var y = setInterval(function () {

    document.getElementById("hintText").innerHTML = "<big>Did you know?</big><br><small>" +
        tips[Math.round((Math.random() * 8))] + "</small>";

}, 10000);
