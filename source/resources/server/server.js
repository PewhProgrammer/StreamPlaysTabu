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
    socket.emit('/connection-server-external', {time: new Date(), text: 'Connected to the server.'});
    socket.emit('connection-server-core', {time: new Date(), text: 'Connected to the server.'});
    socket.on(base2Ext + '/prevotedCategories', function (data) {
        send( base2Ext + '/prevotedCategories', data);
    });
    socket.on(base2Ext + '/giver', function (data) {
        send( base2Ext + '/giver', data);
    });
    socket.on(base2Ext + '/close', function (data) {
        send( base2Ext + '/close', data);
    });
    socket.on(base2Ext + '/guesses', function (data) {
        send( base2Ext + '/guesses', data);
    });
    socket.on(base2Ext + '/explainWord', function (data) {
        send( base2Ext + '/explainWord', data);
    });
    socket.on(base2Ext + '/tabooWords', function (data) {
        send( base2Ext + '/tabooWords', data);
    });
    socket.on(base2Ext + '/question', function (data) {
        send( base2Ext + '/question', data);
    });
    socket.on(base2Ext + '/chatMessage', function (data) {
        send( base2Ext + '/chatMessage', data);
    });
    socket.on(base2Ext + '/validation', function (data) {
        send( base2Ext + '/validation', data);
    });
});

function send(target, data) {
    io.sockets.emit(target, data);
}

console.log("Server is running at port: " + conf.port);
