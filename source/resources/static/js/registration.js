var timeLeft = 30;
// Update the count down every 1 second
var x = setInterval(function() {

    timeLeft = timeLeft - 1;

    document.getElementById("countdown").innerHTML = timeLeft + "s";
    document.getElementById("progressbar").style.width = (timeLeft / 30) * 100 + "%";

    if (timeLeft == 0) {
        timeLeft = 30;
    }
}, 1000);

function updateRanking(ranking) {

}