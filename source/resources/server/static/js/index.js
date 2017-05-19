var giver = "igotabot";
var state = 'Waiting For Giver';
var pw;
var timeLeft = 105;
var templateId = 0;

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#pw").click(function () {
        onPassword();
    });
    $("#category").click(function () {
        onCategoryChosen();
    });
    $("#skip").click(function () {
        onSkip();
    });
    $("#explanation").click(function () {
        onExplanation();
    });
    $("#answer").click(function () {
        onAnswer();
    });
    $("#validation").click(function () {
        onValidation();
    });
});

// Update the count down every 1 second
function runTimer() {

    var x = setInterval(function () {

        timeLeft = timeLeft - 1;

        document.getElementById("progressbar").innerHTML = timeLeft + "s";
        document.getElementById("progressbar").style.width = (timeLeft / 105) * 100 + "%";

        if (timeLeft == 0) {
            document.getElementById("progressbar").innerHTML = "Time's-Up!";
            document.getElementById("progressbar").style.color = "#111111";
            clearInterval(x);
        }
    }, 1000);
}

function onPassword() {
    if (state == 'Waiting For Giver') {
        sendPassword(createPasswordEvent());
    } else {
        window.alert('Too late, son!');
    }
}

function onGiverJoined() {
    sendGiverJoined(createGiverJoinedEvent());
    requestPrevotedCategories(createPrevotedCategoriesRequest());
}

function chosenCat1() {
    showGame();
    onCategoryChosen(getElemeteById("category1").innerHTML);
}

function chosenCat2() {
    onCategoryChosen(getElemeteById("category2").innerHTML);
    showGame();
}

function chosenCat3() {
    onCategoryChosen(getElemeteById("category3").innerHTML);
    showGame();
}

function chosenCat4() {
    onCategoryChosen(getElemeteById("category4").innerHTML);
    showGame();
}

function chosenCat5() {
    onCategoryChosen(getElemeteById("category5").innerHTML);
    showGame();
}

function onCategoryChosen(category) {
    sendCategory(createChosenCategoryEvent());
    requestGiverInfo(createGiverInfoRequest());
    requestValidation(createValidationRequest());
    sendExplanation(createExplanationEvent(category));
}

function onExplanation() {
    sendExplanation(createExplanationEvent($("#explanationText").val()));
}

function onAnswer() {
    sendAnswer(createAnswerEvent());
}

function onSkip() {
    requestSkip(createSkipRequest());
}

function onValidation() {
    sendValidation(createValidationEvent());
}

function createGiverJoinedEvent() {
    return JSON.stringify({
        'password': pw
    });
}

function createPrevotedCategoriesRequest() {
    return JSON.stringify({
        'password': pw
    });
}

function createGiverInfoRequest() {
    return JSON.stringify({
        'password': pw
    });
}

function createValidationRequest() {
    return JSON.stringify({
        'password': pw
    });
}

function createSkipRequest() {
    return JSON.stringify({
        'password': pw
    });
}

function createChosenCategoryEvent() {
    var chosenCategory = $("input[name = PrevotedCategories]:checked").val();
    document.getElementById("categoryLabel").innerHTML = "Chosen Category: " + chosenCategory;
    return JSON.stringify({
        'category': chosenCategory,
        'password': pw
    });
}

function createExplanationEvent(exp) {
    document.getElementById("explanationLabel").innerHTML = "Last Explanation: " + exp;
    return JSON.stringify({
        'giver': giver,
        'explanation': exp,
        'password': pw
    });
}

function createAnswerEvent() {

    var q = $("input[name = Questions]:checked").val();
    var a = $("#answerText").val();
    document.getElementById("answerLabel").innerHTML = "Question: " + q + "; Answer: " + a;
    return JSON.stringify({
        'q': q,
        'a': a,
        'password': pw
    });
}

function createPasswordEvent() {
    pw = $("#pwInput").val();
    return JSON.stringify({
        'password': pw
    });
}

function createValidationEvent() {
    //TODO grab validation info
    return JSON.stringify({
        'password': pw
    });
}

function validatePW(password) {
    return password == pw.toString();
}

function showCategories() {
    document.getElementById("signin").style.visibility = "hidden";
    document.getElementById("signin").style.zIndex = "-1";
    document.getElementById("categories").style.visibility = "visible";
    document.getElementById("categories").style.zIndex = "1";
}

function showGame() {
    document.getElementById("categories").style.visibility = "hidden";
    document.getElementById("categories").style.zIndex = "-2";
    document.getElementById("transparentBG").style.visibility = "hidden";
    document.getElementById("transparentBG").style.zIndex = "0";
    document.getElementById("gameDiv").style.zIndex = "1";

    runTimer();
}

function handleTemplateLayer(layer) {
    document.getElementById('template_layer1').style.display = 'none';
    document.getElementById('template_layer' + layer).style.display = 'block';
    document.getElementById('template_back').style.display = 'block';
}

function handleTemplateLayerPrevious(layer) {
    document.getElementById('template_layer1').style.display = 'block';
    document.getElementById('template_layer' + layer).style.display = 'none';
    document.getElementById('template_back').style.display = 'none';
}

$('.selectpicker').selectpicker('val', 'Mustard');

function handleTemplateDropDown(description, id) {
    $('.selectpicker').selectpicker('val', 'Mustard');
    templateId = id;
    var sel = document.getElementById('sel1');
    var length = sel.options.length;
    var opt1 = document.createElement("option");
    sel.textContent = description;
    for (i = 0; i < length; i++) {
        sel.options[i] = null;
    }
    opt1.text = description;
    sel.add(opt1, sel[0]);
}

function handleTemplateDropDown2(description, description2, id) {
    templateId = id;
    var sel = document.getElementById('sel1');
    var length = sel.options.length;
    for (i = 0; i < length; i++) {
        sel.options[i] = null;
    }

    var opt1 = document.createElement("option");
    var opt2 = document.createElement("option");
    opt1.text = description;
    opt2.text = description2;
    sel.textContent = description;
    sel.add(opt1, sel[0]);
    sel.add(opt2, sel[1]);
}
