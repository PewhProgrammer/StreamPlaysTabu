var base = '/connection-server-external';

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

    document.getElementById("cat1label").innerHTML = json.cat1;
    document.getElementById("cat2").value = json.cat2;
    document.getElementById("cat2label").innerHTML = json.cat2;
    document.getElementById("cat3").value = json.cat3;
    document.getElementById("cat3label").innerHTML = json.cat3;
    document.getElementById("cat4").value = json.cat4;
    document.getElementById("cat4label").innerHTML = json.cat4;
    document.getElementById("cat5").value = json.cat5;
    document.getElementById("cat5label").innerHTML = json.cat5;

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
    document.getElementById("guesses").innerHTML = json.guess1 + ":" + json.nr1 + "<br>"+ json.guess2 + ":" + json.nr2 + "<br>"+ json.guess3 + ":" + json.nr3 + "<br>"
        + json.guess4 + ":" + json.nr4 + "<br>"+ json.guess5 + ":" + json.nr5 + "<br>"+ json.guess6 + ":" + json.nr6 + "<br>"+ json.guess7 + ":" + json.nr7 + "<br>"+ json.guess8 + ":" + json.nr8 + "<br>";
}

function showExplainWord(explainWord) {
    var json =  JSON.parse(explainWord);
    console.log('>> Received explain word: ' + explainWord);
    document.getElementById("explainWord").innerHTML = 'ExplainWord: ' + json.explainWord;
}

function showTabooWords(tabooWords) {
    var json =  JSON.parse(tabooWords);
    console.log('>> Received taboo words: ' + tabooWords);
    document.getElementById("tabooWords").innerHTML = 'TabooWords: ' + json.word1 + ", " + json.word2 + ", " + json.word3 + ", " + json.word4 + ", " + json.word5;
}

function sendExplanation(explanation) {
    console.log('<< Send explanation.');
    sendToServer('/sendExplanation', explanation);
}

function showQuestion(question) {
    var json = JSON.parse(question);
    console.log('>> Received question: ' + question);
    document.getElementById("q1").value = json.question;
    document.getElementById("q1Label").innerHTML = json.question;
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
    console.log('>> RECEIVED gameState: ' + gameState);
    state = gameState.gameState;
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
