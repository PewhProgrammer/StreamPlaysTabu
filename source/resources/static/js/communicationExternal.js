var stompClientExt = null;

function connectExt() {
    //TODO: how to connect server and local client
    var socket = new SockJS('/connection-external-socket');
    stompClientExt = Stomp.over(socket);
    stompClientExt.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClientExt.subscribe('/externalJS/prevotedCategories', function (prevotedCategories) {
            console.log('Received PrevotedCategories: ' + prevotedCategories);
        });
        stompClientExt.subscribe('/externalJS/giver', function (giverInfo) {
            console.log('Received GiverInformation: ' + giverInfo);
        });
        stompClientExt.subscribe('/externalJS/close', function (status) {
            console.log('Game Close with status: ' + status);
        });
        stompClientExt.subscribe('/externalJS/guesses', function (guesses) {
            console.log('Received Guesses: ' + guesses);
        });
        stompClientExt.subscribe('/externalJS/explainWord', function (explainWord) {
            console.log('Received ExplainWord: ' + explainWord);
        });
        stompClientExt.subscribe('/externalJS/tabooWords', function (tabooWords) {
            console.log('Received TabooWords: ' + tabooWords);
        });
        stompClientExt.subscribe('/externalJS/question', function (question) {
            console.log('Received Question: ' + question);
        });
    });
}

function send(target, content) {
    stompClientExt.send(
        "/externalJava" + target,
        {},
        content
    );
}