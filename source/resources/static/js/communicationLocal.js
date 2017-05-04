var stompClient = null;

function connect() {
    var socket = new SockJS('/connection-local-socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/localJS/register', function (gamemode) {
            forwardToRegister(JSON.parse(gamemode.body).content);
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