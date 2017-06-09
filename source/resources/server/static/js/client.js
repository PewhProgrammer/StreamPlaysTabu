var socket;
var base = '/connection-server-external';

$(document).ready(function () {

    socket = io.connect();

    socket.on(base, function (data) {
        console.log('Connected to server: ' + JSON.stringify(data));
    });
    socket.on(base + '/pwSucc', function (data) {
        var json = JSON.parse(data);
        var pass = json["password"];
        if (pass == pw_cmp.toString()) {
            console.log('Password accepted.');
            pw = pw_cmp;
            onGiverJoined();
            showCategories();
        }
    });
    //depricated use
    socket.on(base + '/pwErr', function (data) {
        var json = JSON.parse(data);
        var pass = json["password"];
    });
    socket.on(base + '/prevotedCategories', function (data) {
        if (vpw) {
            showPrevotedCategories(JSON.stringify(data));
        }
    });
    socket.on(base + '/giver', function (data) {
        if (vpw) {
            showGiverInfo(JSON.stringify(data));
        }
    });
    socket.on(base + '/close', function (data) {
        if (vpw) {
            closeGame(JSON.stringify(data));
        }
    });
    socket.on(base + '/guesses', function (data) {
        if (vpw) {
            showGuesses(JSON.stringify(data));
        }
    });
    socket.on(base + '/explainWord', function (data) {
        if (vpw) {
            showExplainWord(JSON.stringify(data));
        }
    });
    socket.on(base + '/tabooWords', function (data) {
        if (vpw) {
            showTabooWords(JSON.stringify(data));
        }
    });
    socket.on(base + '/question', function (data) {
        if (vpw) {
            showQuestion(JSON.stringify(data));
        }
    });
    socket.on(base + '/chatMessage', function (data) {
        if (vpw) {
            showChatMessage(JSON.stringify(data));
        }
    });
    socket.on(base + '/validation', function (data) {
        if (vpw) {
            showValidation(JSON.stringify(data));
        }
    });
    socket.on(base + '/state', function (data) {
        if (vpw) {
            updateGameState(JSON.stringify(data));
        }
    });
    socket.on(base + '/error', function (data) {
        if (vpw) {
            var json = JSON.parse(JSON.stringify(data));
            window.alert(json.msg);
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
