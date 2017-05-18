var giver = "igotabot";
var state = 'Waiting For Giver';
var pw;

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#pw" ).click(function() { onPassword(); });
    $( "#category" ).click(function() { onCategoryChosen(); });
    $( "#skip" ).click(function() { onSkip(); });
    $( "#explanation" ).click(function() { onExplanation(); });
    $( "#answer" ).click(function() { onAnswer(); });
    $( "#validation" ).click(function() { onValidation(); });
});

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
    onCategoryChosen(getElemeteById("category1").innerHTML);
}

function chosenCat2() {
    onCategoryChosen(getElemeteById("category2").innerHTML);
}

function chosenCat3() {
    onCategoryChosen(getElemeteById("category3").innerHTML);
}

function chosenCat4() {
    onCategoryChosen(getElemeteById("category4").innerHTML);
}

function chosenCat5() {
    onCategoryChosen(getElemeteById("category5").innerHTML);
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
    return JSON.stringify({'password' : pw});
}

function createPrevotedCategoriesRequest() {
    return JSON.stringify({'password' : pw});
}

function createGiverInfoRequest() {
    return JSON.stringify({'password' : pw});
}

function createValidationRequest() {
    return JSON.stringify({'password' : pw});
}

function createSkipRequest() {
    return JSON.stringify({'password' : pw});
}

function createChosenCategoryEvent() {
    var chosenCategory = $("input[name = PrevotedCategories]:checked").val();
    document.getElementById("categoryLabel").innerHTML = "Chosen Category: " + chosenCategory;
    return JSON.stringify({
        'category' : chosenCategory,
        'password' : pw
    });
}

function createExplanationEvent(exp) {
    document.getElementById("explanationLabel").innerHTML = "Last Explanation: " + exp;
    return JSON.stringify({
        'giver' : giver,
        'explanation' : exp,
        'password' : pw
    });
}

function createAnswerEvent() {

    var q = $("input[name = Questions]:checked").val();
    var a = $("#answerText").val();
    document.getElementById("answerLabel").innerHTML = "Question: " + q + "; Answer: " + a;
    return JSON.stringify({
        'q' : q,
        'a' : a,
        'password' : pw
    });
}

function createPasswordEvent(){
    pw = $("#pwInput").val();
    document.getElementById("pwLabel").innerHTML = "Sent pw: " + pw;
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