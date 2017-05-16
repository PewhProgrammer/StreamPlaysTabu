var socket;
var base = '/connection-server-external';

$(document).ready(function() {

    socket = io.connect();

    socket.on(base, function(data) {
        console.log('Connected to server: ' + JSON.stringify(data));
    });
    socket.on(base + '/pwSucc', function(data) {
        if (vpw) {
            console.log('Password accepted.');
            onGiverJoined();
        }
    });
    socket.on(base + '/pwErr', function(data) {
        if (vpw) {
            window.alert('Incorrect password. Please try again.');
        }
    });
    socket.on(base + '/prevotedCategories', function(data) {
        if (vpw) {
            showPrevotedCategories(JSON.stringify(data));
        }
    });
    socket.on(base + '/giver', function(data) {
        if (vpw) {
            showGiverInfo(JSON.stringify(data));
        }
    });
    socket.on(base + '/close', function(data) {
        if (vpw) {
            closeGame(JSON.stringify(data));
        }
    });
    socket.on(base + '/guesses', function(data) {
        if (vpw) {
            showGuesses(JSON.stringify(data));
        }
    });
    socket.on(base + '/explainWord', function(data) {
        if (vpw) {
            showExplainWord(JSON.stringify(data));
        }
    });
    socket.on(base + '/tabooWords', function(data) {
        if (vpw) {
            showTabooWords(JSON.stringify(data));
        }
    });
    socket.on(base + '/question', function(data) {
        if (vpw) {
            showQuestion(JSON.stringify(data));
        }
    });
    socket.on(base + '/chatMessage', function(data) {
        if (vpw) {
            showChatMessage(JSON.stringify(data));
        }
    });
    socket.on(base + '/validation', function(data) {
        if (vpw) {
            showValidation(JSON.stringify(data));
        }
    });
});

function send(target, content) {
    socket.emit(target, content);
}

function vpw(data) {
    var json = JSON.parse(data);
    var pw = json["password"];
    return validatePW(pw);
}


