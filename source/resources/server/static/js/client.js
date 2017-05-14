var socket;
var base = '/connection-server-external';

$(document).ready(function() {

    socket = io.connect();

    socket.on(base, function(data) {
        console.log('Connected to server: ' + JSON.stringify(data));
    });
    socket.on(base + '/prevotedCategories', function(data) {
        showPrevotedCategories(JSON.stringify(data));
    });
    socket.on(base + '/giver', function(data) {
        showGiverInfo(JSON.stringify(data));
    });
    socket.on(base + '/close', function(data) {
        closeGame(JSON.stringify(data));
    });
    socket.on(base + '/guesses', function(data) {
        showGuesses(JSON.stringify(data));
    });
    socket.on(base + '/explainWord', function(data) {
        showExplainWord(JSON.stringify(data));
    });
    socket.on(base + '/tabooWords', function(data) {
        showTabooWords(JSON.stringify(data));
    });
    socket.on(base + '/question', function(data) {
        showQuestion(JSON.stringify(data));
    });
    socket.on(base + '/chatMessage', function(data) {
        showChatMessage(JSON.stringify(data));
    });
    socket.on(base + '/validation', function(data) {
        showValidation(JSON.stringify(data));
    });

    onGiverJoined();
});

function send(target, content) {
    socket.emit(target, content);
}


