var $http = new XHR();

function onGet() {

	var request = {
		low : {
			package : 'jmlv.org.Metryc.Test',
			method : 'alert(Integer[])',
			data : [[1,2,3]]
		}
	// ,
	// low: {
	// package: 'Helloinator',
	// method: 'sayHi',
	// data: ['Lascario', [2, 5]]
	// }
	}

	var request2 = {
		high : {
			package : 'jmlv.org.Metryc.Test',
			method : 'alert(String, Integer[][])',
			data : ["HIGH",[[1,3],2,3]]
		}
	// ,
	// low: {
	// package: 'Helloinator',
	// method: 'sayHi',
	// data: ['Lascario', [2, 5]]
	// }
	}

	for (var i = 0; i < 100; i++) {
		// request.low.data[0]++;
		//request2.high.data[0]++;
		$.ajax({
			url : "./MetrycTest",
			type : "GET",
			dataType : "json",
			data : {
				request : JSON.stringify(request)
			},
			success : function(data) {
				console.log(data);
			},
			error : function(err) {
				console.log(err);
			}
		});

		$.ajax({
			url : "./MetrycTest",
			type : "GET",
			dataType : "json",
			data : {
				request : JSON.stringify(request2)
			},
			success : function(data) {
				console.log(data);
			},
			error : function(err) {
				console.log(err);
			}
		});

		setTimeout(function() {
		}, 1000);
	}

}

function onGet2() {
	for (var i = 0; i < 100; i++) {
		// request.low.data[0]++;
		$.ajax({
			url : "./NoMetryc",
			type : "GET",
			data : {
				"opt" : "0"
			},
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			success : function(data) {
				console.log(data);
			},
			error : function(err) {
				console.log(err);
			}
		});

		$.ajax({
			url : "./NoMetryc",
			type : "GET",
			data : {
				"opt" : "1"
			},
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			success : function(data) {
				console.log(data);
			},
			error : function(err) {
				console.log(err);
			}
		});

		setTimeout(function() {
		}, 1000);
	}
}
function onGet3() {

	var request = {
		low : {
			package : 'jmlv.org.Metryc.Test',
			method : 'alert(String)',
			data : [ "LOW" ]
		}
	// ,
	// low: {
	// package: 'Helloinator',
	// method: 'sayHi',
	// data: ['Lascario', [2, 5]]
	// }
	}

	var request2 = {
		high : {
			package : 'jmlv.org.Metryc.Test',
			method : 'alert(Integer)',
			data : [10]
		}
	// ,
	// low: {
	// package: 'Helloinator',
	// method: 'sayHi',
	// data: ['Lascario', [2, 5]]
	// }
	}
	function m() {
		for (var i = 0; i < 200; i++) {
			// request.low.data[0]++;
			request2.high.data[0]++;
			sendMessage(JSON.stringify(request2));
			sendMessage(JSON.stringify(request));
		}
	}
	// var t0 = performance.now();
	m();
	// var t1 = performance.now();
	// console.log("Finish: "+(t1-t0)+" ms");
}
function test() {
	console.log($http.getData());
}
var time = 0;
function connect() {
	var URL = 'ws://localhost:9000/Metryc/metryc';
	if ('WebSocket' in window) {
		ws = new WebSocket(URL);
	} else if ('MozWebSocket' in window) {
		ws = new MozWebSocket(URL);
	} else {
		alert('Tu navegador no soporta WebSockets');
		return;
	}
	ws.onopen = function() {
		// pintamos mensaje
		console.log('Concectado!');
	};
	ws.onmessage = function(event) {
		var message = event.data;
		// pintamos mensaje
		console.log(message);
		var t1 = performance.now();
		time += (t1 - t0);
		console.log("Finish: " + (time) + " S");

	};
	ws.onclose = function() {
		// pintamos mensaje
		console.log('Desconectado!');
	};
	ws.onerror = function(event) {
		console.log('Se produjo un error! ');
	};
}

function disconnect() {
	if (ws != null) {
		ws.close();
		ws = null;
	}
}

var t0 = performance.now();
function sendMessage(message) {
	if (ws != null) {
		ws.send(message);
	}
}

function init() {
	connect();
}

init();
