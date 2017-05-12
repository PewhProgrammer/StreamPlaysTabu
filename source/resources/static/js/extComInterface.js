var base = '/externalJava';


function sendGiverJoined(giverJoinedEvent) {
    console.log('<< Send giver joined.');
    sendToClient('/giverJoined', giverJoinedEvent);
}

function requestPrevotedCategories(request) {
    console.log('<< Request prevoting categories');
    sendToClient('/reqPrevotedCategories', request);
}

function showPrevotedCategories(prevotedCategories) {
    console.log('>> Received prevoting categories: ' + prevotedCategories);
}

function sendCategory(chosenCategory) {
    console.log('<< Send chosen category.');
    sendToClient('/sendCategory', chosenCategory);
}

function requestGiverInfo(request) {
    console.log('<< Request giver information.');
    sendToClient('/reqGiver', request);
}

function showGiverInfo(giverInfo) {
    console.log('>> Received giver information: ' + giverInfo);
}

function showGuesses(guesses) {
    console.log('>> Received guesses: ' + guesses);
}

function showExplainWord(explainWord) {
    console.log('>> Received explain word: ' + explainWord);
}

function showTabooWords(tabooWords) {
    console.log('>> Received taboo words: ' + tabooWords);
}

function sendExplanation(explanation) {
    console.log('<< Send explanation.');
    sendToClient('/sendExplanation', explanation);
}

function showQuestion(question) {
    console.log('>> Received question: ' + question);
}

function sendAnswer(QandA) {
    console.log('<< Send answer.');
    sendToClient('/sendQandA', QandA);
}

function requestSkip(request) {
    console.log('<< Request skip.');
    sendToClient('/reqSkip', request);
}

function requestValidation(request) {
    console.log('<< Request validation.');
    sendToClient('/reqValidation', request);
}

function showValidation(validation) {
    console.log('>> Received validation: ' + validation);
}

function sendValidation(validation) {
    console.log('<< Send validation.');
    sendToClient('/sendValidation', validation);
}

function showChatMessage(msg) {
    console.log('>> Received chat message: ' + msg);
}

function closeGame(status) {
    console.log('>> Received end of game: ' + status);
}

function sendToClient(target, content) {
    send(base + target, content);
}
