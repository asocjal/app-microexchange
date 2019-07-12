let socket = new WebSocket("ws://80.211.81.166:8080/api");

socket.onopen = function(e) {
  // alert("[open] Connection established, send -> server");
  socket.send(buildCommand("GetInfoCommand", {}));
  socket.send(buildCommand("NewPaymentCommand", buildNewPaymentCommandRequest()));
};

socket.onmessage = function(event) {
  // alert(`[message] Data received: ${event.data} <- server`);
  dat = process(event.data);
  injectData(dat);
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

function injectData(data) {
  if(document.getElementById("error").hidden == false) {
    // nothing to do. We have error already
    return;
  }
  if(data.type === "error") {
    injectErrorDetails(data);
    document.getElementById("error").hidden = false;
    return;
  }
  if(data.name.includes("GetInfoCommand")) {
    injectGetInfoCommandData(data);
  } else if(data.name.includes("NewPaymentCommand")) {
    injectNewPaymentCommandData(data);
  }
}

function injectGetInfoCommandData(data) {
  document.getElementById("exName").textContent = data.body.name;
  document.getElementById("email").textContent = data.body.email;
  document.getElementById("email").href = "mailto:" + data.body.email;
}

function injectNewPaymentCommandData(data) {
  document.getElementById("amount1").textContent = data.body.amount + " zł";
  document.getElementById("amount2").textContent = data.body.amount + " zł";
  document.getElementById("ok").hidden = false;
}

function injectErrorDetails(data) {
  text = "";
  let err = data.body;
  while(err != null) {
    text = text + err.message + "<br>";
    err = err.cause;
  }
  document.getElementById("errorDetails").innerHTML = text;
}

socket.onerror = function(error) {
  alert(`[error] ${error.message}`);
};

function buildNewPaymentCommandRequest() {
  var body = {};
  body.calculationId = "calc_4906277691219858432";
  body.lnInvoice = "lnbc1pwjrwvqpp5qjsc3rnnylvzrz2ze3nz8fz7v4ykyljx0xdqdkhc3yfjjgc5qm7sdqqcqzpgea7xw2y4fxp5azm64wcznaknaetzvvvswweyqhle2dr8gj0j4cn5sz5f27azztkrfj7wumgr9u5ssu4lc66hdcwphu7a9q6um8fc38sqrs2j3y";
  return body;
}

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

function process(response)
{
	return JSON.parse(response)
}