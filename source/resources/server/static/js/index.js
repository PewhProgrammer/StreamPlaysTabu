var giver = "igotabot";
var state = 'Waiting For Giver';
var pw;
var pw_cmp;
var timeLeft = 105;
var templateId = 0;
var tempString = "";
var templateLayer = 0;

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

function chosenCat1() { <<
    <<
    <<
    <
    HEAD
    showGame(); ===
    ===
    =
    loadingIndicator(); >>>
    >>>
    >
    fa2330f2fac00e62e37056a500fe18753fa402f9
    onCategoryChosen(document.getElementById("category1").innerHTML);
}

function chosenCat2() {
    loadingIndicator();
    onCategoryChosen(document.getElementById("category2").innerHTML);
}

function chosenCat3() {
    loadingIndicator();
    onCategoryChosen(document.getElementById("category3").innerHTML);
}

function chosenCat4() {
    loadingIndicator();
    onCategoryChosen(document.getElementById("category4").innerHTML);
}

function chosenCat5() {
    loadingIndicator();
    onCategoryChosen(document.getElementById("category5").innerHTML);
}

function loadingIndicator() {
    document.getElementById("indicator").style.visibility = "visible";
}

function onCategoryChosen(category) {
    sendCategory(createChosenCategoryEvent(category));
    requestGiverInfo(createGiverInfoRequest());
    requestValidation(createValidationRequest());
    sendExplanation(createExplanationEvent(category));
}

function onExplanation() {
    if ($("#explanationText").val() != null) {
        document.getElementById('sendButton').style.display = 'none';
        document.getElementById('template_layer1').style.display = 'block';
        document.getElementById('template_layer' + templateLayer).style.display = 'none';

        var result = "";
        if (templateId < 4 && templateId >= 1) {
            result += tempString;
            result += " " + $("#input2").val();
            result += " is";
            result += " " + $("#input3").val();

        } else if (templateId === 12 || templateId === 16) {
            result += $("#sel1").val()
            result += " " + $("#explanationText").val()
        } else {
            result = tempString;
            result += " " + $("#explanationText").val();
        }
        console.log("Sent explanation: " + result);
        if (activeField == "templates") {
            sendExplanation(createExplanationEvent(result));
        } else {
            sendAnswer(createAnswerEvent(result));
            questions[activeQuestion] = null;
            refreshQuestions();
            activeQuestion = -1;
        }
    }
}

function onAnswer() {
    sendAnswer(createAnswerEvent());
}

function onSkip() {
    console.log("haha");
    requestSkip(createSkipRequest());
}

function onValidation(explain, taboo, score) {
    sendValidation(createValidationEvent(explain, taboo, score));
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

function createChosenCategoryEvent(category) {
    return JSON.stringify({
        'category': category,
        'password': pw
    });
}

function createExplanationEvent(exp) {
    document.getElementById("explanationLabel").innerHTML = build + "Last Explanation: " + exp;
    return JSON.stringify({
        'giver': giver,
        'explanation': exp,
        'password': pw
    });
}

function createAnswerEvent(answer) {

    var q = questions[activeQuestion];
    var a = answer;
    return JSON.stringify({
        'q': q,
        'a': a,
        'password': pw
    });
}

function createPasswordEvent() {
    pw_cmp = $("#pwInput").val();
    return JSON.stringify({
        'password': pw_cmp
    });
}

function createValidationEvent(explain, taboo, score) {
    return JSON.stringify({
        'password': pw,
        'reference': explain,
        'taboo': taboo,
        'score': score,
    });
}

function validatePW(password) {
    return password == pw.toString();
}

function showCategories() {
    console.log("wta");
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
    templateLayer = layer;
    document.getElementById('template_layer1').style.display = 'none';
    document.getElementById('template_layer' + layer).style.display = 'block';
}

function handleTemplateLayerPrevious(layer) {
    document.getElementById('sendButton').style.display = 'none';
    document.getElementById('template_layer1').style.display = 'block';
    document.getElementById('template_layer' + layer).style.display = 'none';
}

$('.selectpicker').selectpicker('val', 'Mustard');

function handleTemplateDropDown(description, id) {
    //$('.selectpicker').selectpicker('val', 'Mustard');
    templateId = id;
    tempString = description;
    document.getElementById('sendButton').style.display = 'block';
    document.getElementById('template_dropDownDiv1').style.display = 'block';
    document.getElementById('template_dropDownDiv2').style.display = 'none';
    var sel = document.getElementById('sel1');
    document.getElementById('sel1').disabled = true;
    var length = sel.options.length;
    sel.textContent = description;
    for (i = 0; i < length; i++) {
        sel.options[i] = null;
    }
    var opt1 = document.createElement("option");
    opt1.text = description;
    sel.add(opt1, sel[0]);


}

function handleTemplateDropDown2(description, description2, id) {
    templateId = id;
    tempString = description;
    document.getElementById('sendButton').style.display = 'block';
    document.getElementById('template_dropDownDiv1').style.display = 'block';
    document.getElementById('template_dropDownDiv2').style.display = 'none';
    var sel = document.getElementById('sel1');
    document.getElementById('sel1').disabled = false;
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

function handleTemplateDropDownMultiple(description, description2, description3, id) {
    templateId = id;
    document.getElementById('sendButton').style.display = 'block';
    document.getElementById('template_dropDownDiv1').style.display = 'block';
    document.getElementById('template_dropDownDiv2').style.display = 'none';
    var sel = document.getElementById('sel1');
    document.getElementById('sel1').disabled = false;
    var length = sel.options.length;
    for (i = 0; i < length; i++) {
        sel.options[i] = null;
    }

    var opt1 = document.createElement("option");
    var opt2 = document.createElement("option");
    var opt3 = document.createElement("option");
    opt1.text = description;
    sel.textContent = description;
    sel.add(opt1, sel[0]);
    opt2.text = description2;
    sel.add(opt2, sel[1]);
    opt3.text = description3;
    sel.add(opt3, sel[2]);
}

function handleTemplateDropDownDouble(description, description2, id) {
    templateId = id;
    tempString = description;
    document.getElementById('sendButton').style.display = 'block';
    document.getElementById('template_dropDownDiv2').style.display = 'block';
    document.getElementById('template_dropDownDiv1').style.display = 'none';

    var sel = document.getElementById('drop2');
    var sel2 = document.getElementById('drop3');
    var length = sel.options.length;
    var opt1 = document.createElement("option");
    var opt2 = document.createElement("option");
    sel.textContent = description;
    for (i = 0; i < length; i++) {
        sel2.options[i] = null;
        sel.options[i] = null;
    }
    opt2.text = description2;
    opt1.text = description;
    sel.add(opt1, sel[0]);
    sel2.add(opt2, sel2[0]);
}

function handleStars(id, count) {
    console.log("id: " + id + " , count: " + count);
    var label = "";
    if (id === 1) label = "one";
    if (id === 2) label = "two";
    if (id === 3) label = "three";
    var cat = document.getElementById('validationCategoryLabel_' + label).textContent;
    var taboo = document.getElementById('validationTabooLabel_' + label).textContent;
    document.getElementById('validationCategoryLabel_' + label).textContent = "Thanks a lot!";
    document.getElementById('validationTabooLabel_' + label).textContent = "You've gained 20s more!";
    document.getElementById('stars_' + label).style.display = 'none';
    onValidation(cat, taboo, count);

}
