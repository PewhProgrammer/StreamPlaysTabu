var base = '/connection-server-external';
var questions = [];
var activeQuestion = -1;
var numQuestions = 0;
var activeField = "templates";

function sendGiverJoined(giverJoinedEvent) {
    console.log('<< Send giver joined.');
    sendToServer('/giverJoined', giverJoinedEvent);
}

function requestPrevotedCategories(request) {
    console.log('<< Request prevoting categories');
    sendToServer('/reqPrevotedCategories', request);
}

function showPrevotedCategories(prevotedCategories) {
    console.log('>> Received prevoting categories: ' + prevotedCategories);
    var json = JSON.parse(prevotedCategories);

    if(json.i1 !== "Random choice") document.getElementById("category1").innerHTML = json.cat1 + "<br><br><small>Votes: " + json.i1 + "</small>";
    else document.getElementById("category1").innerHTML = json.cat1 + "<br><br><small>" + json.i1 + "</small>";
    if(json.i2 !== "Random choice") document.getElementById("category2").innerHTML = json.cat2 + "<br><br><small>Votes: " + json.i2 + "</small>";
    else document.getElementById("category2").innerHTML = json.cat2 + "<br><br><small>" + json.i2 + "</small>";
    if(json.i3 !== "Random choice") document.getElementById("category3").innerHTML = json.cat3 + "<br><br><small>Votes: " + json.i3 + "</small>";
    else document.getElementById("category3").innerHTML = json.cat3 + "<br><br><small>" + json.i3 + "</small>";
    if(json.i4 !== "Random choice") document.getElementById("category4").innerHTML = json.cat4 + "<br><br><small>Votes: " + json.i4 + "</small>";
    else document.getElementById("category4").innerHTML = json.cat4 + "<br><br><small>" + json.i4 + "</small>";
    if(json.i5 !== "Random choice") document.getElementById("category5").innerHTML = json.cat5 + "<br><br><small>Votes: " + json.i5 + "</small>";
    else document.getElementById("category5").innerHTML = json.cat5 + "<br><br><small>" + json.i5 + "</small>";
}

function sendCategory(chosenCategory) {
    console.log('<< Send chosen category: ' + chosenCategory);
    sendToServer('/sendCategory', chosenCategory);
}

function requestGiverInfo(request) {
    console.log('<< Request giver information.');
    sendToServer('/reqGiver', request);
}

function showGiverInfo(giverInfo) {
    var json = JSON.parse(giverInfo);
    console.log('>> Received giver information: ' + giverInfo);
    giver = json.name;
}

function showGuesses(guesses) {
    var json = JSON.parse(guesses);
    console.log('>> Received guesses: ' + guesses);

    if (json.guess1 != "") {
        if (json.guess4 != "") {
            document.getElementById("firstGuess").innerHTML = json.guess1 + "<br><br>" + json.guess4;
        } else {
            document.getElementById("firstGuess").innerHTML = json.guess1;
        }
    }
    if (json.guess2 != "") {
        if (json.guess5 != "") {
            document.getElementById("secondGuess").innerHTML = json.guess2 + "<br><br>" + json.guess5;
        } else {
            document.getElementById("secondGuess").innerHTML = json.guess2;
        }
    }
    if (json.guess3 != "") {
        if (json.guess6 != "") {
            document.getElementById("thirdGuess").innerHTML = json.guess3 + "<br><br>" + json.guess6;
        } else {
            document.getElementById("thirdGuess").innerHTML = json.guess3;
        }
    }
}

function showExplainWord(explainWord) {
    var json = JSON.parse(explainWord);
    console.log('>> Received explain word: ' + explainWord);
    document.getElementById("explainWord").innerHTML = json.explainWord;
}

function showTabooWords(tabooWords) {
    var json = JSON.parse(tabooWords);
    console.log('>> Received taboo words: ' + tabooWords);
    var taboo = "";
    if(json.word1 === "" && json.word2 === "" && json.word3 === "" && json.word4 === "" && json.word5 === "") {
        taboo = "No taboo words"
    } else {
        if (json.word1 != "") {
            taboo = "<ul><li>" + json.word1 + "</li>";
        }
        if (json.word2 != "") {
            taboo = "<li>" + json.word2 + "</li>";
        }
        if (json.word3 != "") {
            taboo = "<li>" + json.word3 + "</li>";
        }
        if (json.word4 != "") {
            taboo = "<li>" + json.word4 + "</li>";
        }
        if (json.word5 != "") {
            taboo = "<li>" + json.word5 + "</li></ul>";
        }
    }

    document.getElementById("tabooWords").innerHTML = taboo;
    showGame();
}

function sendExplanation(explanation) {
    console.log('<< Send explanation.');
    sendToServer('/sendExplanation', explanation);
    document.getElementById("templatesDiv").innerHTML = document.getElementById("templatesDiv").innerHTML
        + "<div id='successfullySendPanel' class='panel panel-success' style='position: absolute; top: 47.5%; width: 75%; left: 12.5%; z-index: 5; display: none'><div class='panel-heading' style='text-align: center'>Explanation successfully sent!</div></div>";
    $("#successfullySendPanel").fadeIn("slow").delay(1000).fadeOut("slow", function() {
        $(this).remove();
    });
}

function showQuestion(question) {
    var json = JSON.parse(question);
    console.log('>> Received question: ' + question);
    numQuestions++;
    questions.push(json.question);

    refreshQuestions();
}

function refreshQuestions() {
    var html = "";
    for (var i = questions.length - 1; i >= 0; i--) {
        if (questions[i] != null) {
            html = html + "<p class='questions' onclick='chosenQuestion(" + i + ", this)' id='question" + i + "'>Question:<br>" + questions[i] + "</p>"
        }
    }
    document.getElementById("qAndAs").innerHTML = html;
}

function chosenQuestion(num, p) {
    if (activeQuestion > -1) {
        document.getElementById("question" + activeQuestion).classList.remove("activeQuestion");
    }
    activeQuestion = num;
    document.getElementById(p.id).classList.add("activeQuestion");
    document.getElementById("YesNo").style.visibility = "visible";
    showTemplates();
}

function sendAnswer(QandA) {
    console.log('<< Send answer.');
    sendToServer('/sendQandA', QandA);
    document.getElementById("templatesDiv").innerHTML = document.getElementById("templatesDiv").innerHTML
        + "<div id='successfullySendPanel' class='panel panel-success' style='position: absolute; top: 47.5%; width: 75%; left: 12.5%; z-index: 5; display: none'><div class='panel-heading' style='text-align: center'>Answer successfully sent!</div></div>";
    $("#successfullySendPanel").fadeIn("slow").delay(1000).fadeOut("slow", function() {
        $(this).remove();
    });
}

function requestSkip(request) {
    console.log('<< Request skip.');
    sendToServer('/reqSkip', request);
}

function requestValidation(request) {
    console.log('<< Request validation.');
    sendToServer('/reqValidation', request);
}

function showValidation(validation) {
    console.log('>> Received validation: ' + validation);
    var json = JSON.parse(validation);

    var labelOne = document.getElementById("validationCategoryLabel_one");
    if(json.reference1 != 'EMPTY'){
    document.getElementById("val1").style.visibility = "visible";
    } else if(json.reference2 != 'EMPTY'){
            document.getElementById("val2").style.visibility = "visible";
    }else if(json.reference3 != 'EMPTY'){
    document.getElementById("val3").style.visibility = "visible";
    } else {
        document.getElementById("valHeader").innerHTML = "<b> There is currently no need for " +
        " validation :)";
    }

    labelOne.textContent = json.reference1;
                document.getElementById("validationCategoryLabel_two").textContent = json.reference2;
        document.getElementById("validationCategoryLabel_three").textContent = json.reference3;

    //First validation refers to explain word only
    document.getElementById("validationTabooLabel_two").textContent = json.taboo2;
    document.getElementById("validationTabooLabel_three").textContent = json.taboo3;

}

function updateGameState(gameState) {
    var json = JSON.parse(gameState);
    console.log('>> RECEIVED gameState: ' + json.state);
    state = json.state;
}

function sendValidation(validation) {
    console.log('<< Send validation.');
    sendToServer('/sendValidation', validation);
}

function showChatMessage(msg) {
    var json = JSON.parse(msg);

    var element = document.getElementById("chat");
    console.log('>> Received chat message: ' + json.content + "from: " + json.sender);
    element.innerHTML = element.innerHTML + "<br>" + json.sender + ": " + json.content;

    $('#chat').animate({
        scrollTop: $('#chat').offset().top
    }, "slow");
}

function closeGame(status) {
    console.log('>> Received end of game: ' + status);

    socket.onclose = function () {console.log("Socket closed.")};
    socket.close();

    document.getElementById("progressbar").innerHTML = "GameOver!";
    document.getElementById("progressbar").style.color = "#111111";

    clearInterval(timer);
    hideTemplates();

    var json = JSON.parse(status);
    if (json.status === "Win") {
        document.getElementById("endGameDiv").innerHTML = "<p>You won!<br>+" + json.points + "</p>";
    } else if (json.status === "Lose") {
        document.getElementById("endGameDiv").innerHTML = "<p>Game over!</p>";
    } else if (json.status === "Kick") {
        document.getElementById("endGameDiv").innerHTML = "<p>Too many cheating attempts!<br>Round is over.</p>";
    }
}

function showTemplates(){
    var contentDiv = document.getElementById("endGameDiv");

    document.getElementById("tempDiv").style.visibility = "visible";
    document.getElementById("tempDiv").style.zIndex = "1";
    contentDiv.style.zIndex = "0";
    contentDiv.style.visibility = "hidden";
}

function hideTemplates(){
    var contentDiv = document.getElementById("endGameDiv");

    document.getElementById("tempDiv").style.visibility = "hidden";
    document.getElementById("tempDiv").style.zIndex = "0";
    document.getElementById("YesNo").style.visibility = "hidden";

    contentDiv.style.zIndex = "1";
    contentDiv.style.visibility = "visible";
    contentDiv.innerHTML = "<p>Choose either your explain word on the left or<br>a new question on the right side!</p>";
}

function sendPassword(pw) {
    console.log('<< SEND password: ' + pw);
    sendToServer('/pw', pw);
}

function sendToServer(target, content) {
    send(base + target, content);
}


function chooseExpl() {
    document.getElementById("qAndABlock").style.zIndex = "1";
    document.getElementById("qAndABlock").style.visibility = "visible";

    document.getElementById("cardBlock").style.zIndex = "-1";
    document.getElementById("cardBlock").style.visibility = "hidden";
    document.getElementById("YesNo").style.visibility = "hidden";

    activeField = "templates";
    activeQuestion = -1;
    refreshQuestions();

    showTemplates();
}

function chooseqAndA() {
    document.getElementById("qAndABlock").style.zIndex = "-1";
    document.getElementById("qAndABlock").style.visibility = "hidden";

    document.getElementById("cardBlock").style.zIndex = "1";
    document.getElementById("cardBlock").style.visibility = "visible";

    document.getElementById("YesNo").style.visibility = "visible";
    activeField = "questions";

    hideTemplates();
}
