var gameMode = "Free for all";
var platform = "Twitch";
var channel = "StreamPlaysTaboo";

function setGameMode(mode) {
    gameMode = mode;
    console.log('Set game mode to ' + gameMode);
    document.getElementById('dropdownMode').innerHTML = gameMode + ' <span class="caret"></span>';
}

function setPlatform(pf) {
    platform = pf;
    console.log('Set platform to ' + platform);
    document.getElementById('dropdownPlatform').innerHTML = platform + ' <span class="caret"></span>';
}

function setChannel() {
    channel = document.getElementById('channelName').value;
    console.log('Set channel to ' + channel);
}

function sendSetup() {
    stompClientLoc.send(
        "/localJava/startGame",
        {},
        JSON.stringify({
            'gameMode': gameMode,
            'platform': platform,
            'channel': channel
        })
    );
}

function loadPage() {
    $('#botText').css('display', 'block');
    $('#botText').animate({
        opacity: 0
    }, 0);

    $("#heading").fadeIn();
    $("#h2").delay(200).fadeIn();
    $("#mode").delay(400).fadeIn();
    $("#platforms").delay(600).fadeIn();
    $("#channelName").delay(800).fadeIn();
    $("#startGame").delay(1000).fadeIn();
    $("#botText").delay(1200).animate({
        opacity: 1,
        top: "+=5%"
    }, 1000);

    connectLoc();
}