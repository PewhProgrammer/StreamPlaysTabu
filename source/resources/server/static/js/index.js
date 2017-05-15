var giver = "igotabot";

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#category" ).click(function() { onCategoryChosen(); });
    $( "#skip" ).click(function() { onSkip(); });
    $( "#explanation" ).click(function() { onExplanation(); });
    $( "#answer" ).click(function() { onAnswer(); });
    $( "#validation" ).click(function() { onValidation(); });
});

function onGiverJoined() {
    sendGiverJoined(createGiverJoinedEvent());
    requestPrevotedCategories(createPrevotedCategoriesRequest());
}

function onCategoryChosen() {
    var chosenCategory = $("input[name = PrevotedCategories]:checked").val();
    document.getElementById("categoryLabel").innerHTML = "Chosen Category: " + chosenCategory;
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
    return JSON.stringify({});
}

function createPrevotedCategoriesRequest() {
    return JSON.stringify({});
}

function createGiverInfoRequest() {
    return JSON.stringify({});
}

function createValidationRequest() {
    return JSON.stringify({});
}

function createSkipRequest() {
    return JSON.stringify({});
}

function createChosenCategoryEvent() {
    return JSON.stringify({'category' : chosenCategory});
}

function createExplanationEvent() {
    var explanation = $("#explanationText").val();
    document.getElementById("explanationLabel").innerHTML = "Last Explanation: " + explanation;
    return JSON.stringify({
        'giver' : giver,
        'explanation' : explanation
    });
}

function createAnswerEvent() {

    var q = $("input[name = Questions]:checked").val();
    var a = $("#answerText").val();
    document.getElementById("answerLabel").innerHTML = "Question: " + q + "; Answer: " + a;
    return JSON.stringify({
        'q' : q,
        'a' : a
    });
}

function createValidationEvent() {
    //TODO implement
}