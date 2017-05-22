var stompClientLoc = null;
var gameState = "Register";

function connectLoc() {
    var socket = new SockJS('/connection-local-socket');
    stompClientLoc = Stomp.over(socket);
    stompClientLoc.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClientLoc.subscribe('/localJS/gameMode', function (gamemode) {
           updateMode(gamemode);
        });
        stompClientLoc.subscribe('/localJS/gameState', function (gamestate) {
            updateState(gamestate);
        });
        stompClientLoc.subscribe('/localJS/score', function(ranking) {
            updateRanking(ranking);
        });
        stompClientLoc.subscribe('/localJS/prevoteCategory', function(categories) {
            updateCategoryVote(categories)
        });
        stompClientLoc.subscribe('/localJS/giver', function(giver) {
            updateGiver(giver);
        });
        stompClientLoc.subscribe('/localJS/guesses', function(guesses) {
           updateGuesses(guesses);
        });
        stompClientLoc.subscribe('/localJS/explanations', function(explanations) {
            updateExplanations(explanations);
        });
        stompClientLoc.subscribe('/localJS/qAndA', function(qAndA) {
           updateQandA(qAndA);
        });
        stompClientLoc.subscribe('/localJS/validation', function(validation) {
            updateValidation(validation);
        });
        stompClientLoc.subscribe('/localJS/endGame', function(endGame) {
            updateEndGame(endGame);
        });
        stompClientLoc.subscribe('/localJS/category', function(categorychosen) {
            categoryChosen(categorychosen);
        });
        stompClientLoc.subscribe('/localJS/err', function(error) {
            window.alert(error);

        })

        if (window.location.href == 'http://localhost:8080/registerFFA.html') {
            prepareRegister();
        }
        if (window.location.href == 'http://localhost:8080/registerSE.html') {
            prepareRegister();
        }
        if (window.location.href == 'http://localhost:8080/game.html') {
            prepareGame();
        }
    });
}

function disconnect() {
    if (stompClientLoc != null) {
        stompClientLoc.disconnect();
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
    stompClientLoc.send(
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
        case "Waiting For Giver": {
            document.getElementById("statusInfo").innerHTML = "Waiting for chosen giver!";
            timeLeft = 30;
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
