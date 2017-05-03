/**
 * Created by Marc on 02.05.2017.
 */
var timeLeft = 90;
// Update the count down every 1 second
var timer = setInterval(function() {

    timeLeft = timeLeft - 1;

    document.getElementById("countdown").innerHTML = timeLeft + "s";
    document.getElementById("progressbar").style.width = (timeLeft / 90) * 100 + "%";

    if (timeLeft == 0) {
        document.getElementById("countdown").innerHTML = "Time's up!";
        clearInterval(timer);
    }
}, 1000);
