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
        if (window.location.href == 'http://localhost:8080/registerFFA.html') {
            prepareRegister();
        }
        if (window.location.href == 'http://localhost:8080/game.html') {
            prepareGame();
        }
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

function updateState(gamestate) {
    gameState = JSON.parse(gamestate.body).gameState;

    switch (gameState) {
        case "Game Started": {
            document.location.href = "/game.html";
            break;
        }
        case "Waiting for giver": {
            twenties = true;
            timeLeft = 20;
            break;
        }
        case "Win": {
            window.alert('Winner winner chicken dinner!');
            break;
        }
        case "Lose": {
            window.alert('Loser loser chicken doser!');
            break;
        }
        default: { //case Register
            if (gameMode == "Free for all") {
                document.location.href = "/registerFFA.html";
            } else {
                document.location.href = "/registerSE.html";
            }
        }
    }
}
