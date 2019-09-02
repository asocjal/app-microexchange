var url = new URL(window.location.href);

let socket = new WebSocket("wss://" + url.hostname + "/api");

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
    injectErrorString(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
  } else {
    // e.g. server process killed or network down
    // event.code is usually 1006 in this case
    injectErrorString('[close] Connection died');
  }
};


function injectData(data) {
  if(document.getElementById("error").hidden == false) {
    // nothing to do. We have error already
    return;
  }
  if(data.type === "error") {
    injectErrorDetails(data);
    return;
  }
  if(data.name.includes("GetInfoCommand")) {
    injectGetInfoCommandData(data);
  } else if(data.name.includes("NewPaymentCommand")) {
    injectNewPaymentCommandData(data);
  } else if(data.name.includes("GetPaymentStatusCommand")) {
    injectPaymentStatusCommandData(data);
  }
}

function injectGetInfoCommandData(data) {
  document.getElementById("exName").textContent = data.body.name;
  document.getElementById("email").textContent = data.body.email;
  document.getElementById("email").href = "mailto:" + data.body.email;
}

function injectNewPaymentCommandData(data) {
  document.getElementById("amount1").textContent = data.body.amount.toFixed(2) + " zł";
  document.getElementById("amount2").textContent = data.body.amount.toFixed(2) + " zł";
  document.getElementById("bankNumber").textContent = data.body.accountNumber;
  document.getElementById("bankUrl").href = data.body.bank.pageUrl;
  document.getElementById("bankLogo").href = data.body.bank.iconUrl;
  document.getElementById("title").textContent = data.body.title;
  document.getElementById("payee").textContent = data.body.payee;
  
  document.getElementById("ok").hidden = false;

  window.setInterval(function(){
    var body = {};
    body.title = document.getElementById("title").textContent;
    socket.send(buildCommand("GetPaymentStatusCommand", body));
  }, 100);
}

function injectPaymentStatusCommandData(data) {
  document.getElementById("status").textContent = data.body.status;
  document.getElementById("timeLeft").textContent = data.body.timeLeft;
  // clearInterval() 
}

function injectErrorDetails(data) {
  text = "";
  let err = data.body;
  while(err != null) {
    text = text + err.message + "<br>";
    err = err.cause;
  }
  document.getElementById("errorDetails").innerHTML = text;
  document.getElementById("error").hidden = false;
  document.getElementById("ok").hidden = true;
}

function injectErrorString(data) {
  document.getElementById("errorDetails").innerHTML = data;
  document.getElementById("error").hidden = false;
  document.getElementById("ok").hidden = true;
}

socket.onerror = function(error) {
  injectErrorString(`[error] ${error.message}`);
};

function buildNewPaymentCommandRequest() {
  var body = {};
  body.calculationId = url.searchParams.get("calculationId");
  body.bankId = url.searchParams.get("bankId");
  body.lnInvoice = url.searchParams.get("lnInvoice");

  document.getElementById("lnInvoice").innerHTML = body.lnInvoice;
  
  return body;
}

function buildCommand(name, body) {
    var cmd = {};
    cmd.id = randomInt();
    cmd.name = "net.satopay.satoexchange.web.commands." + name;
    cmd.type = "request";
    cmd.timeout = 10000;
    cmd.body = body;
    // alert(JSON.stringify(cmd));
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