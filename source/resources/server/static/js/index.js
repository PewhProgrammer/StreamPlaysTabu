var giver = "igotabot";
var state = 'Waiting For Giver';
var pw;
var pw_cmp;
var timeLeft = 105;
var timeMax = 105;
var templateId = 0;
var tempString = "";
var templateLayer = 0;
var timer;
var tempUsage = [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2];

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#pw").click(function () {
        pw_cmp = getParameterByName('pw');
        console.log(pw_cmp);
        if (pw_cmp === "root") {
            onCategoryChosen(document.getElementById("category1").innerHTML);
            $("#transparentBG").hide();
            $("#signin").hide();
            showGame();
        } else
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

app.get('/', function (req, res) {
    var n = req.param('pw');
    pw = n;
});

// Update the count down every 1 second
function runTimer() {

    timer = setInterval(function () {


        $('li').toggleClass('nth-child(1)');

        timeLeft = timeLeft - 1;

        document.getElementById("progressbar").innerHTML = timeLeft + "s";
        document.getElementById("progressbar").style.width = (timeLeft / timeMax) * 100 + "%";

        if (timeLeft <= 0) {
            document.getElementById("progressbar").innerHTML = "Time's-Up!";
            document.getElementById("progressbar").style.color = "#111111";
            clearInterval(timer);
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
    loadingIndicator();
    onCategoryChosen(document.getElementById("category1").innerHTML.split("<br>")[0]);
}

function chosenCat2() {
    loadingIndicator();
    onCategoryChosen(document.getElementById("category2").innerHTML.split("<br>")[0]);
}

function chosenCat3() {
    loadingIndicator();
    onCategoryChosen(document.getElementById("category3").innerHTML.split("<br>")[0]);
}

function chosenCat4() {
    loadingIndicator();
    onCategoryChosen(document.getElementById("category4").innerHTML.split("<br>")[0]);
}

function chosenCat5() {
    loadingIndicator();
    onCategoryChosen(document.getElementById("category5").innerHTML.split("<br>")[0]);
}

function loadingIndicator() {
    document.getElementById("indicator").style.visibility = "visible";
}

function onCategoryChosen(category) {
    sendCategory(createChosenCategoryEvent(category));
    requestGiverInfo(createGiverInfoRequest());
    requestValidation(createValidationRequest());
    console.log('<< SEND explanation: ' + 'The word to be explained is from the category ' + category);
    sendExplanation(createExplanationEvent('The word to be explained is from the category ' + category));
}

function onExplanation() {
    if (tempUsage[templateId] === 0) {
        //already used twice
        return;
    } else {
        tempUsage[templateId] -= 1;
    }

    if (templateId === 24 || templateId === 25) {
        document.getElementById('sendButton').style.display = 'none';
        document.getElementById('template_layer1').style.display = 'block';

        sendAnswer(createAnswerEvent(tempString));
        questions[activeQuestion] = null;
        refreshQuestions();
        activeQuestion = -1;
    } else if ($("#explanationText").val() != "" || ($("#input2").val() != "" && $("#input3").val() != "")) {
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
        } else if (activeQuestion > 1) {
                sendAnswer(createAnswerEvent(result));
                questions[activeQuestion] = null;
                refreshQuestions();
                activeQuestion = -1;
                hideTemplates();
            }
    }

    document.getElementById("explanationText").innerHTML = "";
    document.getElementById("input2").innerHTML = "";
    document.getElementById("input3").innerHTML = "";
}

function onAnswer() {
    sendAnswer(createAnswerEvent());
}

function onSkip() {
    requestSkip(createSkipRequest());
}

function onValidation(explain, taboo, score,id) {
    sendValidation(createValidationEvent(explain, taboo, score,id));
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
    document.getElementById("explanationText").innerHTML = "Last Explanation: " + exp;
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
    pw_cmp = getParameterByName('pw');
    if (pw_cmp.length === 5 && pw_cmp.substring(5) === "") {
        pw_cmp = pw_cmp.substring(0, 4);
    }
    return JSON.stringify({
        'password': pw_cmp
    });
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function createValidationEvent(explain, taboo, score,id) {
    return JSON.stringify({
        'password': pw,
        'reference': explain,
        'taboo': taboo,
        'score': score,
        'id': id,
    });
}

function validatePW(password) {
    return true;
}

function showCategories() {
    console.log("wta");
    $("#signin").fadeOut();
    $("#categories").show();
    document.getElementById("categories").style.zIndex = "1";

    $('#slickheading').css('display', 'block');
    $('#slickheading').animate({
        opacity: 0
    }, 0);
    $('#slickheading').animate({
        opacity: 1,
        top: "-=2%"
    }, 800);

    $("#category1").delay(400).fadeIn("slow");
    $("#category3").delay(600).fadeIn("slow");
    $("#category5").delay(800).fadeIn("slow");
    $("#category4").delay(1000).fadeIn("slow");
    $("#category2").delay(1200).fadeIn("slow");

    runTimer();
}

function showGame() {
    document.getElementById("categories").style.visibility = "hidden";
    document.getElementById("categories").style.zIndex = "-2";
    document.getElementById("transparentBG").style.visibility = "hidden";
    document.getElementById("transparentBG").style.zIndex = "0";
    document.getElementById("gameDiv").style.zIndex = "1";
}

function handleTemplateLayer(layer) {
    templateLayer = layer;
    document.getElementById('template_layer1').style.display = 'none';
    document.getElementById('template_layer' + layer).style.display = 'block';

    document.getElementById("explanationText").innerHTML = "";
    document.getElementById("input2").innerHTML = "";
    document.getElementById("input3").innerHTML = "";
}

function handleTemplateLayerPrevious(layer) {
    document.getElementById("explanationText").innerHTML = "";
    document.getElementById("input2").innerHTML = "";
    document.getElementById("input3").innerHTML = "";

    document.getElementById('sendButton').style.display = 'none';
    document.getElementById('template_layer1').style.display = 'block';
    document.getElementById('template_layer' + layer).style.display = 'none';
}

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

function handleTemplateYesNo(value, id) {
    if (activeQuestion > -1) {
        templateId = id;
        tempString = value;
        onExplanation();
    }
}

function handleTemplateDropDown2(description, description2, id) {
    document.getElementById("explanationText").innerHTML = "";

    templateId = id;
    tempString = description;
    showTemplateUsage(tempUsage[templateId]);
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
    document.getElementById("explanationText").innerHTML = "";


    templateId = id;
        showTemplateUsage(tempUsage[templateId]);
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
    document.getElementById("input2").innerHTML = "";
    document.getElementById("input3").innerHTML = "";

    templateId = id;
        showTemplateUsage(tempUsage[templateId]);
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
    if (id === 1) {
        label = "one";
        document.getElementById("val1").style.visibility = "hidden";
        document.getElementById("val1").style.zIndex = "-2";
        document.getElementById("val3").style.zIndex = "-1";
        document.getElementById("val2").style.zIndex = "0";
        document.getElementById("val2").style.visibility = "visible";
        document.getElementById("valHeader").innerHTML = "Does the taboo word below fit to its explain word? "
        + "<br>You have gained +10 seconds extra time already!";
    }
    if (id === 2) {
        label = "two";
        document.getElementById("val2").style.visibility = "hidden";
        document.getElementById("val1").style.zIndex = "-1";
        document.getElementById("val3").style.zIndex = "0";
        document.getElementById("val2").style.zIndex = "-2";
        document.getElementById("val3").style.visibility = "visible";
        document.getElementById("valHeader").innerHTML = "Does the explain word below fit to its category? "
        + "<br>You have gained +20 seconds extra time already!";
    }
    if (id === 3) {
        label = "three";
        document.getElementById("val3").style.visibility = "hidden";
                document.getElementById("valHeader").innerHTML = "Thank you very much for helping us improve this game!"
                + "<br>You have gained +30 seconds extra time in total!";
    }
    var cat = document.getElementById('validationCategoryLabel_' + label).textContent;
    var taboo = document.getElementById('validationTabooLabel_' + label).textContent;
    document.getElementById('stars_' + label).style.display = 'none';
    timeLeft += 10;
    timeMax += 10;
    onValidation(cat, taboo, count,id);
}

function showTemplateUsage(i){
    var doc = document.getElementById("explanationLabel");
        var doc_2 = document.getElementById("explanationLabel1");
        var label = "Your Explanation: ( you can only use this template "
                         + i + " more times )";

              if(i === 0){
              document.getElementById("explanation").disabled = true;
              label = "You have used this template twice already!";

              }
              else  document.getElementById("explanation").disabled = false;
                  doc.innerHTML = label;
                  doc_2.innerHTML = label;

}
