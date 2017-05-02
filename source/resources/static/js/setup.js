
var gameMode = "Free for all";
var platform = "Twitch";

function setGameMode(mode) {
    gameMode = mode;
    document.getElementById('dropdownMode').innerHTML = gameMode + ' <span class="caret"></span>';
}

function setPlatform(pf) {
    platform = pf;
    document.getElementById('dropdownPlatform').innerHTML = platform + ' <span class="caret"></span>';
}

function startGame() {
    var ch = document.getElementById('channelName').value;
    console.log("Start game at channel " + ch + " on " + platform + " in the " + gameMode + " modus.");

    $.ajax({
        url: "/settings",
        type: "GET",
        dataType: "json",
        data: {
            gameMode: gameMode,
            platform: platform,
            channel: ch
        }
    });
}
