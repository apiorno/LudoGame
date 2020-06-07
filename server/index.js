var Stack= require('./stack').Stack
var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var players = [];
var colorsStack = new Stack();
colorsStack.push(0);
colorsStack.push(1);
colorsStack.push(2);
colorsStack.push(3);

server.listen(8080, function(){
	console.log("Server is now running...");
});

io.on('connection', function(socket){
	console.log("Player Connected!");
	var color = colorsStack.pop();
	socket.emit('socketID', { id: socket.id, color : color });
	socket.emit('getPlayers', players);
	socket.broadcast.emit('newPlayer', { id: socket.id, color : color });
	socket.on('disconnect', function(){
		console.log("Player Disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		colorsStack.push(color);
		for(var i = 0; i < players.length; i++){
			if(players[i].id == socket.id){
				players.splice(i, 1);
			}
		}
	});
	players.push(new player(socket.id, color));
	if(players.length ==4){
	    socket.emit('gameStarted', players);
	    socket.broadcast.emit('gameStarted', players)
	}
});

function player(id, color){
	this.id = id;
	this.color = color;
}