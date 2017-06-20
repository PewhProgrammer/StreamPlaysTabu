var socket;
var base = '/connection-server-external';

$(document).ready(function () {

    socket = io.connect();

    socket.on(base, function (data) {
        //console.log('Connected to server: ' + JSON.stringify(data));
    });
    socket.on(base + '/pwSucc', function (data) {
        var json = JSON.parse(data);
        var pass = json["password"];
        if (pass == pw_cmp.toString()) {
            //console.log('Password accepted.');
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
        if (vpw(data)) {
            showPrevotedCategories(JSON.stringify(data));
        }
    });
    socket.on(base + '/giver', function (data) {
        if (vpw(data)) {
            showGiverInfo(JSON.stringify(data));
        }
    });
    socket.on(base + '/close', function (data) {
        if (vpw(data)) {
            closeGame(JSON.stringify(data));
        }
    });
    socket.on(base + '/guesses', function (data) {
        if (vpw(data)) {
            showGuesses(JSON.stringify(data));
        }
    });
    socket.on(base + '/explainWord', function (data) {
        if (vpw(data)) {
            showExplainWord(JSON.stringify(data));
        }
    });
    socket.on(base + '/tabooWords', function (data) {
        if (vpw(data)) {
            showTabooWords(JSON.stringify(data));
        }
    });
    socket.on(base + '/question', function (data) {
        if (vpw(data)) {
            showQuestion(JSON.stringify(data));
        }
    });
    socket.on(base + '/chatMessage', function (data) {
        if (vpw(data)) {
            showChatMessage(JSON.stringify(data));
        }
    });
    socket.on(base + '/validation', function (data) {
        if (vpw(data)) {
            showValidation(JSON.stringify(data));
        }
    });
    socket.on(base + '/state', function (data) {
        if (vpw(data)) {
            updateGameState(JSON.stringify(data));
        }
    });
    socket.on(base + '/error', function (data) {
        if (vpw(data)) {
            var json = JSON.parse(JSON.stringify(data));
            window.alert(json.msg);
        }
    });
});

function send(target, content) {
    socket.emit(target, content);
}

function vpw(data) {
    //console.log(">> CHECK PW");
    var json = JSON.parse(JSON.stringify(data));
    var pw = json["password"];
    return validatePW(pw);
}