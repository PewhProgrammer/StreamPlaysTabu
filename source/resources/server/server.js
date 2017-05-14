var express = require('express')
    ,   app = express()
    ,   server = require('http').createServer(app)
    ,   io = require('socket.io').listen(server)
    ,   conf = require('./config.json');

server.listen(conf.port);

app.use(express.static(__dirname + '/static'));

app.get('/', function(req, res) {
   res.sendfile(__dirname + '/static/index.html')
});

var base2Ext = '/connection-server-external';
var base2Core = '/connection-server-core';

io.sockets.on('connection', function (socket) {
   initializeCore(socket);
   initializeExt(socket);
});

function initializeExt(socket) {

    socket.on(base2Core, function(data) {
        console.log(data);
    });
    socket.on(base2Core + '/prevotedCategories', function(data) {
       console.log('>> RECEIVED prevotedCategories: ' + JSON.stringify(data));
       send2Ext('/prevotedCategories' , data);
    });
    socket.on(base2Core + '/giver', function(data) {
        console.log('>> RECEIVED giver: ' + JSON.stringify(data));
        send2Ext('/giver', data);
    });
    socket.on(base2Core + '/close', function(data) {
        console.log('>> RECEIVED close: ' + JSON.stringify(data));
        send2Ext('/close', data);
    });
    socket.on(base2Core + '/guesses', function(data) {
        console.log('>> RECEIVED guesses: ' + JSON.stringify(data));
        send2Ext('/guesses', data);
    });
    socket.on(base2Core + '/explainWord', function(data) {
        console.log('>> RECEIVED explainWord: ' + JSON.stringify(data));
        send2Ext('/explainWord', data);
    });
    socket.on(base2Core + '/tabooWords', function(data) {
        console.log('>> RECEIVED tabooWords: ' + JSON.stringify(data));
        send2Ext('/tabooWords', data);
    });
    socket.on(base2Core + '/question', function(data) {
        console.log('>> RECEIVED question: ' + JSON.stringify(data));
        send2Ext('/question', data);
    });
    socket.on(base2Core + '/chatMessage', function(data) {
        console.log('>> RECEIVED chatMessage: ' + JSON.stringify(data));
        send2Ext('/chatMessage', data);
    });
    socket.on(base2Core + '/validation', function(data) {
        console.log('>> RECEIVED validation: ' + JSON.stringify(data));
        send2Ext('/validation', data);
    });

    socket.emit('/connection-server-external', {time: new Date()});

}

function initializeCore(socket) {

    socket.on(base2Ext + '/giverJoined', function (data) {
        console.log('>> SEND giver joined: ' + JSON.stringify(data));
        send2Core('/giverJoined', data);
    });
    socket.on(base2Ext + '/reqPrevotedCategories', function (data) {
        console.log('>> REQUEST prevote categories: ' + JSON.stringify(data));
        send2Core('/reqPrevotedCategories', JSON.stringify(data))
    });
    socket.on(base2Ext + '/sendCategory', function (data) {
        console.log('>> SEND chosen category: ' + JSON.stringify(data));
        send2Core('/sendCategory', data)
    });
    socket.on(base2Ext + '/reqGiver', function (data) {
        console.log('>> REQUEST giver information: ' + JSON.stringify(data));
        send2Core('/reqGiver', data)
    });
    socket.on(base2Ext + '/sendExplanation', function (data) {
        console.log('>> SEND explanation: ' + JSON.stringify(data));
        send2Core('/sendExplanation', data)
    });
    socket.on(base2Ext + '/sendQandA', function (data) {
        console.log('>> SEND QandA: ' + JSON.stringify(data));
        send2Core('/sendQandA', data)
    });
    socket.on(base2Ext + '/reqSkip', function (data) {
        console.log('>> REQUEST skip: ' + JSON.stringify(data));
        send2Core('/reqSkip', data)
    });
    socket.on(base2Ext + '/reqValidation', function (data) {
        console.log('>> REQUEST validation: ' + JSON.stringify(data));
        send2Core('/reqValidation', data)
    });
    socket.on(base2Ext + '/sendValidation', function (data) {
        console.log('>> SEND validation: ' + JSON.stringify(data));
        send2Core('/sendValidation', data)
    });

    socket.emit('/connection-server-core', {time: new Date()});

}

function send2Core(target, data) {
    console.log('<< EMIT ' + JSON.stringify(data) + ' to ' + base2Core + target);
    io.sockets.emit(base2Core + target, data);
}

function send2Ext(target, data) {
    console.log('<< EMIT ' + JSON.stringify(data) + ' to ' + base2Ext + target);
    io.sockets.emit(base2Ext + target, data);
}

console.log("Server is running at port: " + conf.port);
