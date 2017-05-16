var socket;
var base = '/connection-server-external';

$(document).ready(function() {

    socket = io.connect();

    socket.on(base, function(data) {
        console.log('Connected to server: ' + JSON.stringify(data));
    });
    socket.on(base + '/pwSucc', function(data) {
        if (vpw(data)) {
            console.log('Password accepted.');
            onGiverJoined();
        }
    });
    socket.on(base + '/pwErr', function(data) {
        if (vpw(data)) {
            window.alert('Incorrect password. Please try again.');
        }
    });
    socket.on(base + '/prevotedCategories', function(data) {
        if (vpw(JSON.stringify(data))) {
            showPrevotedCategories(JSON.stringify(data));
        }
    });
    socket.on(base + '/giver', function(data) {
        if (vpw(JSON.stringify(data))) {
            showGiverInfo(JSON.stringify(data));
        }
    });
    socket.on(base + '/close', function(data) {
        if (vpw(JSON.stringify(data))) {
            closeGame(JSON.stringify(data));
        }
    });
    socket.on(base + '/guesses', function(data) {
        if (vpw(JSON.stringify(data))) {
            showGuesses(JSON.stringify(data));
        }
    });
    socket.on(base + '/explainWord', function(data) {
        if (vpw(JSON.stringify(data))) {
            showExplainWord(JSON.stringify(data));
        }
    });
    socket.on(base + '/tabooWords', function(data) {
        if (vpw(JSON.stringify(data))) {
            showTabooWords(JSON.stringify(data));
        }
    });
    socket.on(base + '/question', function(data) {
        if (vpw(JSON.stringify(data))) {
            showQuestion(JSON.stringify(data));
        }
    });
    socket.on(base + '/chatMessage', function(data) {
        if (vpw(JSON.stringify(data))) {
            showChatMessage(JSON.stringify(data));
        }
    });
    socket.on(base + '/validation', function(data) {
        if (vpw(JSON.stringify(data))) {
            showValidation(JSON.stringify(data));
        }
    });
});

function send(target, content) {
    socket.emit(target, content);
}

function vpw(data) {
    var pass = JSON.parse(data)["password"];
    return validatePW(pass);
}


