var base = '/connection-server-external';

$(document).ready(function() {

    var socket = io.connect();

    socket.on('/connection-server-external', function(data) {
        console.log(data);
    });

});

/*
function connectExt() {
    var socket = new SockJS('/connection-external-socket');
    stompClientExt = Stomp.over(socket);
    stompClientExt.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClientExt.subscribe('/externalJS/prevotedCategories', function (prevotedCategories) {
            showPrevotedCategories(prevotedCategories);
        });
        stompClientExt.subscribe('/externalJS/giver', function (giverInfo) {
            showGiverInfo(giverInfo);
        });
        stompClientExt.subscribe('/externalJS/close', function (status) {
            closeGame(status);
        });
        stompClientExt.subscribe('/externalJS/guesses', function (guesses) {
            showGuesses(guesses);
        });
        stompClientExt.subscribe('/externalJS/explainWord', function (explainWord) {
            showExplainWord(explainWord);
        });
        stompClientExt.subscribe('/externalJS/tabooWords', function (tabooWords) {
            showTabooWords(tabooWords);
        });
        stompClientExt.subscribe('/externalJS/question', function (question) {
            showQuestion(question);
        });
        stompClientExt.subscribe('/externalJS/chatMessage', function (msg) {
            showChatMessage(msg);
        });
        stompClientExt.subscribe('/externalJS/validation', function (validation) {
            showValidation(validation);
        });

        onGiverJoined();
    });
}

function send(target, content) {
    stompClientExt.send(
        target,
        {},
        content
    );
}
*/

