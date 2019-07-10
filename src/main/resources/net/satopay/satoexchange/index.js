let socket = new WebSocket("ws://80.211.81.166:8080/api");

socket.onopen = function(e) {
  alert("[open] Connection established, send -> server");
  socket.send(buildCommand("GetInfoCommand", {}));
};

socket.onmessage = function(event) {
  alert(`[message] Data received: ${event.data} <- server`);
};

socket.onclose = function(event) {
  if (event.wasClean) {
    alert(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
  } else {
    // e.g. server process killed or network down
    // event.code is usually 1006 in this case
    alert('[close] Connection died');
  }
};

socket.onerror = function(error) {
  alert(`[error] ${error.message}`);
};

function buildCommand(name, body) {
    var cmd = {};
    cmd.id = randomInt();
    cmd.name = "net.satopay.satoexchange.commands." + name;
    cmd.type = "request";
    cmd.timeout = 10000;
    cmd.body = body;
    return JSON.stringify(cmd);
}

function randomInt() {
    var min=1; 
    var max=1000000000;  
    return Math.floor(Math.random() * (+max - +min)) + +min;
}