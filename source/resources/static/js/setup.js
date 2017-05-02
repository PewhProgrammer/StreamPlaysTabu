
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
    stompClient.send(
        "/localJava/startGame",
        {},
        JSON.stringify({
            'gameMode': gameMode,
            'platform': platform,
            'channel': channel
        })
    );
}