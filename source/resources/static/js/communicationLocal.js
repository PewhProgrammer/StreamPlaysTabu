var stompClient = null;
var gameState = "Register";

function connect() {
    var socket = new SockJS('/connection-local-socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/localJS/GameMode', function (gamemode) {
            gameMode = JSON.parse(gamemode.body).content;
            window.alert("Updated game mode to " + gameMode);
            console.log("Updated game mode to " + gameMode);
            updateMode();
        });
        stompClient.subscribe('/localJS/GameState', function (gamestate) {
            gameState = JSON.parse(gamestate.body).gameState;
            window.alert("Updated game state to " + gameState);
            console.log("Updated game state to " + gameState);
            updateScreen();
        });
        stompClient.subscribe('/localJS/score', function(ranking) {
            updateRanking(ranking);
        });
        stompClient.subscribe('/localJS/prevoteCategory', function(categories) {
            updateCategoryVote(categories)
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
