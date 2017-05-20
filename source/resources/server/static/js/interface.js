var base = '/connection-server-external';
var questions = [];
var activeQuestion = -1;
var numQuestions = 0;

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

    document.getElementById("category1").innerHTML = json.cat1;
    document.getElementById("category2").innerHTML = json.cat2;
    document.getElementById("category3").innerHTML = json.cat3;
    document.getElementById("category4").innerHTML = json.cat4;
    document.getElementById("category5").innerHTML = json.cat5;

    showCategories();
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
    document.getElementById("giverInfo").innerHTML = 'Giver: ' + json.name + '; Points: ' + json.points + '; Level: ' + json.level + ';';
}

function showGuesses(guesses) {
    var json = JSON.parse(guesses);
    console.log('>> Received guesses: ' + guesses);

    if(json.guess1 != "") {
        if(json.guess5 != "") {
            document.getElementById("firstGuess").innerHTML = json.guess1 + "<br><br>" + json.guess5;
        } else {
            document.getElementById("firstGuess").innerHTML = json.guess1;
        }
    }
    if(json.guess2 != "") {
        if(json.guess6 != "") {
            document.getElementById("secondGuess").innerHTML = json.guess2 + "<br><br>" + json.guess6;
        } else {
            document.getElementById("secondGuess").innerHTML = json.guess2;
        }
    }
    if(json.guess3 != "") {
        if(json.guess7 != "") {
            document.getElementById("thirdGuess").innerHTML = json.guess3 + "<br><br>" + json.guess7;
        } else {
            document.getElementById("thirdGuess").innerHTML = json.guess3;
        }
    }
    if(json.guess4 != "") {
        if(json.guess8 != "") {
            document.getElementById("fourthGuess").innerHTML = json.guess4 + "<br><br>" + json.guess8;
        } else {
            document.getElementById("fourthGuess").innerHTML = json.guess4;
        }
    }
}

function showExplainWord(explainWord) {
    var json =  JSON.parse(explainWord);
    console.log('>> Received explain word: ' + explainWord);
    document.getElementById("explainWord").innerHTML = 'ExplainWord: ' + json.explainWord;
}

function showTabooWords(tabooWords) {
    var json =  JSON.parse(tabooWords);
    console.log('>> Received taboo words: ' + tabooWords);
    var taboo = "";
    if(json.word1 != "") {
        taboo = "<ul><li>" + json.word1 + "</li>";
    }
    if(json.word2 != "") {
        taboo = "<li>" + json.word2 + "</li>";
    }
    if(json.word3 != "") {
        taboo = "<li>" + json.word3 + "</li>";
    }
    if(json.word4 != "") {
        taboo = "<li>" + json.word4 + "</li>";
    }
    if(json.word5 != "") {
        taboo = "<li>" + json.word5 + "</li></ul>";
    }
    document.getElementById("tabooWords").innerHTML = taboo;
}

function sendExplanation(explanation) {
    console.log('<< Send explanation.');
    sendToServer('/sendExplanation', explanation);
}

function showQuestion(question) {
    var json = JSON.parse(question);
    console.log('>> Received question: ' + question);
    numQuestions++;
    questions.push(json.question);

    var html ="";
    for(var i=questions.length-1; i >= 0; i--) {
        if (questions[i] != null) {
            html = html + "<p class='questions' onclick='chosenQuestion(" + i + ", this)' id='question" + i + "'>Question:<br>" + questions[i] + "</p>"
        }
    }

    document.getElementById("qAndAs").innerHTML = html;
}

function chosenQuestion(num, p) {
    if(activeQuestion > -1){
        document.getElementById("question" + activeQuestion).classList.remove("activeQuestion");
    }
    activeQuestion = num;
    document.getElementById(p.id).classList.add("activeQuestion");
}

function sendAnswer(QandA) {
    console.log('<< Send answer.');
    sendToServer('/sendQandA', QandA);
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
    //TODO display validation information
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
    var json =  JSON.parse(msg);
    console.log('>> Received chat message: ' + msg);
    document.getElementById("chat").value = document.getElementById("chat").value + "<br>" + msg;
}

function closeGame(status) {
    console.log('>> Received end of game: ' + status);
}

function sendPassword(pw) {
    console.log('<< SEND password: ' + pw);
    sendToServer('/pw', pw);
}

function sendToServer(target, content) {
    send(base + target, content);
}


function chooseExpl() {

}

function chooseqAndA() {

}