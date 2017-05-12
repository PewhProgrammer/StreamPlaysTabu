var giver = "dummy giver";

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
    return JSON.stringify({'category' : $("#PrevotedCategories").val()});
}

function createExplanationEvent() {
    return JSON.stringify({
        'giver' : giver,
        'explanation' : $("#explanationText").val()
    });
}

function createAnswerEvent() {
    return JSON.stringify({
        'question' : $("#Questions").val(),
        'answer' : $("#answerText").val()
    });
}

function createValidationEvent() {
    //TODO implement
}