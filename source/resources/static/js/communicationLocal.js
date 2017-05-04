var stompClient = null;

function connect() {
    var socket = new SockJS('/connection-local-socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/localJS/register', function () {
            console.log("localJS");
            document.location = '/templates/registerFFA.html';
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