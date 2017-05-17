var giver = "igotabot";
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
    sendPassword(createPasswordEvent());
}

function onGiverJoined() {
    sendGiverJoined(createGiverJoinedEvent());
    requestPrevotedCategories(createPrevotedCategoriesRequest());
}

function onCategoryChosen() {
    sendCategory(createChosenCategoryEvent());
    requestGiverInfo(createGiverInfoRequest());
    requestValidation(createValidationRequest());
}

function onExplanation() {
    sendExplanation(createExplanationEvent());
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

function createExplanationEvent() {
    var explanation = $("#explanationText").val();
    document.getElementById("explanationLabel").innerHTML = "Last Explanation: " + explanation;
    return JSON.stringify({
        'giver' : giver,
        'explanation' : explanation,
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