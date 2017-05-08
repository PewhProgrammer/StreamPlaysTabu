var stompClient = null;
var gameState = "Register";

function connect() {
    var socket = new SockJS('/connection-local-socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/localJS/gameMode', function (gamemode) {
           updateMode(gamemode);
        });
        stompClient.subscribe('/localJS/gameState', function (gamestate) {
            updateState(gamestate);
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
        stompClient.subscribe('/localJS/validation', function validation(validation) {
            updateValidation(validation);
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
            sendSetup();
        }
    );
});

function updateMode(gamemode) {
    gameState = JSON.parse(gamestate.body).gameState;
    gameMode = JSON.parse(gamemode.body).content;
    console.log('Game mode changed to ' + gameMode + '.');
}

function requestGameMode() {
    stompClient.send(
        "/localJava/reqGameMode",
        {},
        JSON.stringify({})
    );
}

function updateState() {
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
