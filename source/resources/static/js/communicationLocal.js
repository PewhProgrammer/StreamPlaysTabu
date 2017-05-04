var stompClient = null;
var gameState = "Register";

function connect() {
    var socket = new SockJS('/connection-local-socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/localJS/gameMode', function (gamemode) {
            gameMode = JSON.parse(gamemode.body).content;
            updateMode();
        });
        stompClient.subscribe('/localJS/gameState', function (gamestate) {
            gameState = JSON.parse(gamestate.body).gameState;
            updateScreen();
        });
        stompClient.subscribe('/localJS/score', function(ranking) {
            updateRanking(ranking);
        });
        stompClient.subscribe('/localJS/prevoteCategory', function(categories) {
            updateCategoryVote(categories)
        });
        stompClient.subscribe('/localJS/giver', function(giver) {
            updateGiver(giver);
        });
        stompClient.subscribe('/localJS/guesses', function(guesses) {
           updateGuesses(guesses);
        });
        stompClient.subscribe('/localJS/explanations', function(explanations) {
            updateExplanations(explanations);
        });
        stompClient.subscribe('/localJS/qAndA', function(qAndA) {
           updateQandA(qAndA);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#startGame" ).click(
        function() {
            setChannel();
            connect();
            setTimeout(sendSetup, 1000);
        }
    );
});

function updateScreen() {
    if (gameState == "Game Started") {
        document.location.href = "/game.html";
    } else {
        if (gameMode == "Free for all") {
            document.location.href = "/registerFFA.html";
        } else {
            document.location.href = "/registerSE.html";
        }
    }
}
