var app = angular.module("featapp", ["ui.router"]);
app.config(["$stateProvider", "$urlRouterProvider",


    function ($state, $url) {
        $url.otherwise("/home"); // $url.when("/home", "/home/index");
        $state.state("home", {
            "url": "/home"
            , "templateUrl": "views/feat-principal.html"
            , "controller": "HomeCtrl"
        }).state("about", {
            "url": "/about"
            , "templateUrl": "views/feat-conocenos.html"
            , "controller": "AboutCtrl"
        }).state("desktop", {
            "url": "/desktop"
            , "templateUrl": "views/feat-editor.html"
            , "controller": "DesktopCtrl"
        })
    }
]);
var editor; //variable global editor
var metryc; //variable global metryc
var counter = 0
    , doc = new Array()
    , documentlines = 0,
    cursor={line: 0, ch: 1},
    cacheline=null;
dsave = (data) => {
    var textres = data.response.route.split("\\");
    download(data.response.text, textres[textres.length - 1], "text/plain");
}
refresh = (data) => {
    console.log("Actualizacion");
    editor.toTextArea();
    var doclin = new Array();
    var doctext="";
    if(cacheline!=null){
    	doclin = data.response.text.split("\n");
    	console.log("doclin.length: "+(doclin.length)+" cacheline.linecount: "+cacheline.linecount+" cacheline.line: "+cacheline.line);
    	if((doclin.length)<cacheline.linecount){
    		var dif = cacheline.linecount-(doclin.length);
    		console.log("dif menos lineas: "+dif);
    		cacheline.line-=dif;
    	}else if((doclin.length-1)>cacheline.linecount){
    		var dif = (doclin.length)-cacheline.linecount;
    		console.log("dif mas lineas: "+dif);
    		cacheline.line+=dif;
    	}
    	for(var w=0; w<doclin.length;w++){
    		if(w==cacheline.line){
    			if(w==(doclin.length-1)){
    				doctext+=cacheline.text;
    				console.log(doctext);
    			}else{
    			doctext+=cacheline.text+'\n';}
    		}else{
    			if(w==(doclin.length-1)){
    				doctext+=doclin[w];
    				console.log(doctext);

    			}else{
    		doctext+=doclin[w]+'\n'}}
    	}
    
    }else{
    	doctext=data.response.text;
    }
    console.log(doclin.length);
    document.getElementById('testArea').value = doctext;
   
    editor = CodeMirror.fromTextArea(document.getElementById('testArea'), {
        lineNumbers: true
    });
    editor.focus();
    editor.setCursor(cursor);
    var init = true;
    var lines = new Array();
    var from, to, totallines, totallines = editor.lineCount();
    documentlines = editor.lineCount();
    console.log(editor.lineCount());
    doc = [];
    for (var i = 0; i < documentlines; i++) {
        doc[i] = editor.getLine(i);
    }  
    var jos= Math.floor(Math.random() * 6) + 1,
        mich= 0;
    
    editor.on('change',function(editor, cMirror){
        if(mich<jos){mich++;}else{
            var newdocument = new Array();
        for (var i = 0; i < editor.lineCount(); i++) {
            newdocument[i] = editor.getLine(i);
        }
        cursor = null;
        cursor = editor.getCursor();
        console.log(cursor);
        cacheline={line:cursor.line, ch:cursor.ch, text:editor.getLine(cursor.line), linecount:editor.lineCount()};
        console.log(doc);
        checkdocument(doc, newdocument, data.response.id, data.response.route);
        }
//		from = cMirror.from;
//		to= cMirror.to;
//		var newdocument = new Array();
//		for(var i=0; i<documentlines; i++){
//			newdocument[i] = editor.getLine(i);
//		}
//		console.log(doc);
//		checkdocument(doc, newdocument, data.response.id, data.response.route);
//
//		 var line1 = parseInt(from.line);
//		 var line2 = parseInt(to.line);
//		 console.log("change on lines:"+ line1 + " :char "+ from.ch + " - "+(line2)+ " :char "+ to.ch);
	  });
    
//    setTimeout(function() {
//        var newdocument = new Array();
//        for (var i = 0; i < documentlines; i++) {
//            newdocument[i] = editor.getLine(i);
//        }
//        console.log(doc);
//        checkdocument(doc, newdocument, data.response.id, data.response.route);
//    }, 1500);
}

function checkdocument(doc, newdocument, id, route) {
    var lines = new Array();
    var text = new Array();
    console.log(doc.length + " " + newdocument.length);
    if (doc.length == newdocument.length) {
        console.log("same");
        var flag = false;
        for (var i = 0; i < doc.length; i++) {
            console.log(doc[i]);
            console.log(newdocument[i]);
            if (doc[i] == newdocument[i] && flag==false) {
                console.log("son iguales 1");
            }
            else {
                console.log("length: " + lines.length);
                lines[lines.length] = i;
                text[text.length] = newdocument[i];
                flag=true;
            }
        }
    }
    else if (doc.length < newdocument.length) {
        for (var i = 0; i < newdocument.length; i++) {
            if (i > doc.length) {
                lines[lines.length] = i;
                text[text.length] = newdocument[i];
            }
            else {
                console.log(doc[i]);
                console.log(newdocument[i]);
                //				if(doc[i].){}
                if (doc[i] == newdocument[i]) {
                    console.log("son iguales 2");
                }
                else {
                    lines[lines.length] = i;
                    text[text.length] = newdocument[i];
                    console.log("no son iguales 2");
                }
            }
        }
    }
    else if (doc.length > newdocument.length) {
        var flag3 = false;
        for (var i = 0; i < newdocument.length; i++) {
            console.log(doc[i]);
            console.log(newdocument[i]);
            //				if(doc[i].){}
            if (doc[i] == newdocument[i] && (flag3==false)) {
                console.log("son iguales 3");
            }
            else {
                lines[lines.length] = i;
//                if(newdocument[i]){}
                text[text.length] = newdocument[i];
                console.log("no son iguales 3");
                flag3=true;
            }
            
        }
    }
  
    console.log(lines.length);
    for (var j = 0; j < lines.length; j++) {
        console.log("linea: " + lines[j] + " texto:" + text[j]);
    }
    var request8 = {
        getfromgroup: [id, window.localStorage.getItem("id")]
        , request: {
            high: {
                package: 'jmlv.org.Metryc.Editor'
                , method: 'writeDocument(String, String[], Integer[], Integer)'
                , data: [route, text, lines, id]
                , callback: 'refresh'
            }
        }
    }
   
    metryc.sendMessage(JSON.stringify(request8));
}
app.controller("HomeCtrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {
        $(document).ready(() => {
            var regLogName = $("#login-name"), // input nombre
                regLogLastn = $("#login-lastname"), // input apellido
                row1 = $("#r1"), // row de los input nombre y apellido
                regLogEmail = $("#login-username"), // input email
                row2 = $("#r2"), // row del input email
                regLogc1 = $("#login-pass1"), // input contraseña 1
                row3 = $("#r3"), // row del input contraseña 1
                regLogc2 = $("#login-pass2"), // input contraseña 2
                row4 = $("#r4"), // row del input contraseña 2
                row5 = $("#r5"), // row del boton registrate
                registrar = $("#r5b"), // boton registrate
                row6 = $("#r6"), // row del boton continuar registro
                conregistrar = $("#r6b"), // boton continuar registro
                logEmail = $("#enter-email"), // input email login
                logPass = $("#enter-pass"), // input contraseña login
                acceder = $("#accede"), // boton para acceder o hacer login
                alert1 = $("#alert1"), // wrong nombre y apellido en registro
                alert2 = $("#alert2"), // wrong email en registro
                alert3 = $("#alert3"), // wrong password en registro
                alert4 = $("#alert4"), // wrong email y password en inicio
                conocenos = $("#conocenos"); // boton para ir a la pantalla
            // de conocenos
            // click en el boton de conocenos
            conocenos.click(() => {
                $state.go('about');
            }); // funcion que corrobora que se hayan incluido todos los datos para
            // darle
            // click al boton de registrar
            var aux1 = false
                , aux2 = false
                , aux3 = false
                , vacio = "";
            checkReg1 = () => {
                var regname = regLogName.val()
                    , reglastn = regLogLastn.val()
                    , regemail = regLogEmail.val()
                    , pattemail = new RegExp("^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$")
                    , pattnames = new RegExp("^[A-Za-z]{3,15}$");
                if (regname.trim() == vacio) {
                    regLogName.css({
                        "border": "1px solid #d9534f"
                    });
                    alert1.css({
                        "display": "block"
                    });
                    aux1 = true;
                }
                else if (pattnames.test(regname.trim()) == false) {
                    regLogName.css({
                        "border": "1px solid #d9534f"
                    });
                    alert1.css({
                        "display": "block"
                    });
                    aux1 = true;
                }
                if (reglastn.trim() == vacio) {
                    regLogLastn.css({
                        "border": "1px solid #d9534f"
                    });
                    alert1.css({
                        "display": "block"
                    });
                    aux2 = true;
                }
                else if (pattnames.test(reglastn.trim()) == false) {
                    regLogLastn.css({
                        "border": "1px solid #d9534f"
                    });
                    alert1.css({
                        "display": "block"
                    });
                    aux2 = true;
                }
                if (regemail.trim() == vacio) {
                    regLogEmail.css({
                        "border": "1px solid #d9534f"
                    });
                    alert2.css({
                        "display": "block"
                    });
                    aux3 = true;
                }
                else if (pattemail.test(regemail.trim()) == false) {
                    regLogEmail.css({
                        "border": "1px solid #d9534f"
                    });
                    alert2.css({
                        "display": "block"
                    });
                    aux3 = true;
                }
            }
            registrar.click(() => {
                checkReg1();
                if (aux1 == false) {
                    if (aux2 == false) {
                        if (aux3 == false) {
                            alert1.css({
                                "display": "none"
                            });
                            alert2.css({
                                "display": "none"
                            });
                            var emailr1 = regLogEmail.val().trim().toLowerCase()
                                , request = {
                                    low: {
                                        package: 'jmlv.org.Metryc.SignUp'
                                        , method: 'CheckEmail(String)'
                                        , data: [emailr1]
                                    }
                                }
                            $.ajax({
                                url: "./Feat"
                                , type: "POST"
                                , dataType: "json"
                                , data: {
                                    request: JSON.stringify(request)
                                }
                                , success: data => {
                                    if (data.response.email[0].count == 0) {
                                        console.log("no existe el email");
                                        row1.css({
                                            "display": "none"
                                        });
                                        row2.css({
                                            "display": "none"
                                        });
                                        row3.css({
                                            "display": "block"
                                        });
                                        row4.css({
                                            "display": "block"
                                        });
                                        row5.css({
                                            "display": "none"
                                        });
                                        row6.css({
                                            "display": "block"
                                        });
                                    }
                                    else {
                                        console.log("existe el email");
                                        alert1.css({
                                            "display": "none"
                                        });
                                        regLogEmail.css({
                                            "border": "1px solid #d9534f"
                                        });
                                        alert2.css({
                                            "display": "block"
                                        });
                                    }
                                }
                                , error: err => {
                                    console.log(err); // err
                                }
                            });
                        }
                        else {
                            regLogLastn.css({
                                "border": "1px solid #cccccc"
                            });
                            regLogName.css({
                                "border": "1px solid #cccccc"
                            });
                            alert1.css({
                                "display": "none"
                            });
                            aux3 = false;
                        }
                    }
                    else {
                        if (aux3 == false) {
                            regLogName.css({
                                "border": "1px solid #cccccc"
                            });
                            regLogEmail.css({
                                "border": "1px solid #cccccc"
                            });
                            alert2.css({
                                "display": "none"
                            });
                            aux2 = false;
                        }
                        else {
                            regLogName.css({
                                "border": "1px solid #cccccc"
                            });
                            aux2 = false;
                            aux3 = false;
                        }
                    }
                }
                else {
                    if (aux2 == false) {
                        if (aux3 == false) {
                            regLogLastn.css({
                                "border": "1px solid #cccccc"
                            });
                            regLogEmail.css({
                                "border": "1px solid #cccccc"
                            });
                            alert2.css({
                                "display": "none"
                            });
                            aux1 = false;
                        }
                        else {
                            regLogLastn.css({
                                "border": "1px solid #cccccc"
                            });
                            aux1 = false;
                            aux3 = false;
                        }
                    }
                    else {
                        if (aux3 == false) {
                            regLogEmail.css({
                                "border": "1px solid #cccccc"
                            });
                            alert2.css({
                                "display": "none"
                            });
                            aux1 = false;
                            aux2 = false;
                        }
                        else {
                            aux1 = false;
                            aux2 = false;
                            aux3 = false;
                        }
                    }
                }
            }); // funcion que corrobora que se hayan incluido todos los datos para
            // darle
            // click al boton de continuar con el registro
            var aux4 = false
                , aux5 = false;
            checkReg2 = () => {
                var regcon1 = regLogc1.val()
                    , regcon2 = regLogc2.val()
                    , pattpass = new RegExp("(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,12})$");
                if (regcon1.trim() == vacio) {
                    regLogc1.css({
                        "border": "1px solid #d9534f"
                    });
                    alert3.css({
                        "display": "block"
                    });
                    aux4 = true;
                }
                else if (pattpass.test(regcon1.trim()) == false) {
                    regLogc1.css({
                        "border": "1px solid #d9534f"
                    });
                    alert3.css({
                        "display": "block"
                    });
                    console.log(pattpass.test(regcon1.trim()));
                    aux4 = true;
                }
                if (regcon2.trim() == vacio) {
                    regLogc2.css({
                        "border": "1px solid #d9534f"
                    });
                    alert3.css({
                        "display": "block"
                    });
                    aux5 = true;
                }
                else if (pattpass.test(regcon2.trim()) == false) {
                    regLogc2.css({
                        "border": "1px solid #d9534f"
                    });
                    alert3.css({
                        "display": "block"
                    });
                    aux5 = true;
                }
                if (regcon2.trim() != regcon1.trim()) {
                    regLogc1.css({
                        "border": "1px solid #d9534f"
                    });
                    regLogc2.css({
                        "border": "1px solid #d9534f"
                    });
                    alert3.css({
                        "display": "block"
                    });
                    aux5 = true;
                    aux4 = true;
                }
            }
            conregistrar.click(() => {
                checkReg2();
                if (aux4 == false) {
                    if (aux5 == false) {
                        alert3.css({
                            "display": "none"
                        });
                        regLogc1.css({
                            "border": "1px solid #cccccc"
                        });
                        regLogc2.css({
                            "border": "1px solid #cccccc"
                        });
                        var namer2 = regLogName.val().trim().toLowerCase()
                            , lastnr2 = regLogLastn.val().trim().toLowerCase()
                            , emailr2 = regLogEmail.val().trim().toLowerCase()
                            , passr2 = regLogc1.val().trim()
                            , request2 = {
                                low: {
                                    package: 'jmlv.org.Metryc.SignUp'
                                    , method: 'Register(String, String, String, String)'
                                    , data: [namer2, lastnr2, emailr2, passr2]
                                }
                            }
                        $.ajax({
                            url: "./Feat"
                            , type: "POST"
                            , dataType: "json"
                            , data: {
                                request: JSON.stringify(request2)
                            }
                            , success: data => {
                                console.log(data);
                                data = data.response.result[0];
                                window.localStorage.setItem("id", data.id_users);
                                window.localStorage.setItem("lastname", lastnr2);
                                window.localStorage.setItem("name", namer2);
                                window.localStorage.setItem("email", emailr2)
                                $state.go('desktop');
                            }
                            , error: err => {
                                console.log(err); // err
                                alert3.css({
                                    "display": "block"
                                });
                                regLogc1.css({
                                    "border": "1px solid #d9534f"
                                });
                                regLogc2.css({
                                    "border": "1px solid #d9534f"
                                });
                            }
                        });
                    }
                    else {
                        regLogc1.css({
                            "border": "1px solid #cccccc"
                        });
                        aux5 = false;
                    }
                }
                else {
                    if (aux5 == false) {
                        regLogc2.css({
                            "border": "1px solid #cccccc"
                        });
                        aux4 = false;
                    }
                    else {
                        aux4 = false;
                        aux5 = false;
                    }
                }
            }); // funcion que corrobora que se hayan incluido todos los datos para
            // darle
            // click al boton de acceder
            var aux6 = false
                , aux7 = false;
            checkLogin = () => {
                var logemail = logEmail.val().toLowerCase()
                    , logcon = logPass.val()
                    , pattemail2 = new RegExp("^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$")
                    , pattpass2 = new RegExp("(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,12})$");
                if (logemail.trim() == vacio) {
                    logEmail.css({
                        "border": "1px solid #d9534f"
                    });
                    alert4.css({
                        "display": "block"
                    });
                    aux6 = true;
                }
                else if (pattemail2.test(logemail.trim()) == false) {
                    logEmail.css({
                        "border": "1px solid #d9534f"
                    });
                    alert4.css({
                        "display": "block"
                    });
                    aux6 = true;
                }
                if (logcon.trim() == vacio) {
                    logPass.css({
                        "border": "1px solid #d9534f"
                    });
                    alert4.css({
                        "display": "block"
                    });
                    aux7 = true;
                }
                else if (pattpass2.test(logcon.trim()) == false) {
                    logPass.css({
                        "border": "1px solid #d9534f"
                    });
                    alert4.css({
                        "display": "block"
                    });
                    aux7 = true;
                }
            }
            acceder.click(() => {
                checkLogin();
                if (aux6 == false) {
                    if (aux7 == false) {
                        var emaill = logEmail.val().trim().toLowerCase()
                            , passl = logPass.val().trim()
                            , request3 = {
                                low: {
                                    package: 'jmlv.org.Metryc.Login'
                                    , method: 'login(String,String)'
                                    , data: [emaill, passl]
                                }
                            }
                        $.ajax({
                            url: "./Feat"
                            , type: "POST"
                            , dataType: "json"
                            , data: {
                                request: JSON.stringify(request3)
                            }
                            , success: data => {
                                data = data.response.result[0];
                                alert4.css({
                                    "display": "none"
                                });
                                logEmail.css({
                                    "border": "1px solid #cccccc"
                                });
                                logPass.css({
                                    "border": "1px solid #cccccc"
                                });
                                if (data.count == 1) {
                                    window.localStorage.setItem("id", data.id_users);
                                    window.localStorage.setItem("lastname", data.lastname_user);
                                    window.localStorage.setItem("name", data.name_user);
                                    window.localStorage.setItem("email", emaill)
                                    $state.go('desktop');
                                }
                                else {
                                    alert4.css({
                                        "display": "block"
                                    });
                                    logEmail.css({
                                        "border": "1px solid #d9534f"
                                    });
                                    logPass.css({
                                        "border": "1px solid #d9534f"
                                    });
                                }
                            }
                            , error: err => {
                                alert4.css({
                                    "display": "block"
                                });
                                logEmail.css({
                                    "border": "1px solid #d9534f"
                                });
                                logPass.css({
                                    "border": "1px solid #d9534f"
                                });
                                console.log(err); // err
                            }
                        });
                    }
                    else {
                        logEmail.css({
                            "border": "1px solid #cccccc"
                        });
                        aux7 = false;
                    }
                }
                else {
                    if (aux7 == false) {
                        logPass.css({
                            "border": "1px solid #cccccc"
                        });
                        aux6 = false;
                    }
                    else {
                        aux6 = false;
                        aux7 = false;
                    }
                }
            }); // ADICIONAL
            // funcion para cambio de comentarios
            var m1 = $("#m1")
                , m2 = $("#m2")
                , m3 = $("#m3")
                , j1 = $("#j1")
                , j2 = $("#j2")
                , j3 = $("#j3")
                , i = 0;
            comentarios = () => {
                setInterval(() => {
                    i++;
                    if (i == 1) {
                        m2.css({
                            "display": "block"
                        });
                        m3.css({
                            "display": "none"
                        });
                        m1.css({
                            "display": "none"
                        });
                        j2.css({
                            "display": "block"
                        });
                        j3.css({
                            "display": "none"
                        });
                        j1.css({
                            "display": "none"
                        });
                    }
                    else if (i == 2) {
                        m3.css({
                            "display": "block"
                        });
                        m1.css({
                            "display": "none"
                        });
                        m2.css({
                            "display": "none"
                        });
                        j3.css({
                            "display": "block"
                        });
                        j1.css({
                            "display": "none"
                        });
                        j2.css({
                            "display": "none"
                        });
                    }
                    else {
                        m1.css({
                            "display": "block"
                        });
                        m2.css({
                            "display": "none"
                        });
                        m3.css({
                            "display": "none"
                        });
                        j1.css({
                            "display": "block"
                        });
                        j2.css({
                            "display": "none"
                        });
                        j3.css({
                            "display": "none"
                        });
                        i = i - 3;
                    }
                }, 10500);
            }
            setTimeout(() => {
                comentarios();
            }, 500);
        }); // fin ready
    }
]);
app.controller("AboutCtrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {
        $scope.gohome = () => {
            $state.go("home");
        }
        $scope.register = () => {
            $state.go("home");
        }
        $scope.vete = () => {
            $state.go("home");
        }
        request4 = {
            low: {
                package: 'jmlv.org.Metryc.About'
                , method: 'getInfo()'
                , data: []
            }
        }
        $.ajax({
            url: "./Feat"
            , type: "POST"
            , dataType: "json"
            , data: {
                request: JSON.stringify(request4)
            }
            , success: data => {
                data = data.response.Info[0];
                document.getElementById("about-u").innerHTML = '<strong class="fnmb" >' + data.usercount + '</strong>usuarios'
                document.getElementById("about-d").innerHTML = '<strong class="fnmb" >' + data.documentcount + '</strong>documentos creados'
            }
            , error: err => {
                console.log(err); // err
            }
        });
}
]);
app.controller("DesktopCtrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {
    var cambiesc = $("#cambiesc"), // boton para salir del editor
        editor1 = $("#editor"), // row del editor
        escritorio1 = $("#escritorio"), // row del escritorio
        dname = $("#editor-dname"), // input para crear el editor
        titledocument = $("#title-document"), // titulo del documento
        titlechange = $("#title-change"), // input para cambiar el nombre
        alertdoc1 = $("#alertdoc1"), // wrong nombre al crear archivo. ya existe
        alertdoc2 = $("#alertdoc2"), // wrong nombre al crear archivo.vacio
        equisx = $("#equisx"), // equis para eliminar archivo
        alertdoch2 = $("#alertdoch2"), // wrong cambiar nombre de archivo
        botontxt = $("#botontxt"), //boton para descargar archivo en el ordenador
        xgdt = $("#xgdt"); //x del cuadro para administrar el gdt
    metryc = null; //variable global para metryc
    // funcion para mostrar archivos en el escritorio   
    $scope.loadDocuments = () => {
        request6 = {
            low: {
                package: 'jmlv.org.Metryc.Desktop'
                , method: 'Escritorio(Integer)'
                , data: [window.localStorage.getItem("id")]
            }
        }
        $http({
            method: "POST"
            , url: "./Feat"
            , responseType: 'json'
            , data: "request=" + JSON.stringify(request6)
            , headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).success(function (data) {
            if (data.response.escritorio == "0") {
                $scope.documents = [];
            }
            else {
                $scope.documents = data.response.escritorio;
            }
        });
    }
    
    $scope.getUsers = () => {
        var iddoc = document.getElementById("gdt").getAttribute("name");
        request6 = {
            low: {
                package: 'jmlv.org.Metryc.Editor'
                , method: 'getUsers(Integer)'
                , data: [iddoc]
            }
        }
        $http({
            method: "POST"
            , url: "./Feat"
            , responseType: 'json'
            , data: "request=" + JSON.stringify(request6)
            , headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).success(function (data) {
            if (data.response.users == "0") {
                $scope.users = [{
                    "name_user": "No Hay participantes..."
                }];
            }
            else {
                $scope.users = data.response.users;
            }
        });
    }
    $scope.loadDocuments();
    //nombre del usuario y/o archivo del doc en el editor
    MaysPrimera = (cadena) => cadena.charAt(0).toUpperCase() + cadena.slice(1);
    plasmarN = () => {
        if (window.localStorage.getItem("id")) {
            var nameu = window.localStorage.getItem("name")
                , lastnu = window.localStorage.getItem("lastname");
            document.getElementById("editor-n").innerHTML = "Hola, <em>" + MaysPrimera(nameu.toLowerCase()) + " " + MaysPrimera(lastnu.toLowerCase()) + "</em>";
            $scope.loadDocuments();
        }
        else {
            $state.go("home");
        }
    }
    plasmarNyT = (id, route, titulo, profile) => {
        if (window.localStorage.getItem("id")) {
            var nameu = window.localStorage.getItem("name")
                , lastnu = window.localStorage.getItem("lastname");
            document.getElementById("editor-n").innerHTML = "Hola, <em>" + MaysPrimera(nameu.toLowerCase()) + " " + MaysPrimera(lastnu.toLowerCase()) + ". Colabora en la edición del archivo: " + titulo + "</em>";
        }
        else {
            $state.go("home");
        }
        var admin = $('#gdt');
        document.getElementById("gdt").setAttribute('name', id);
        if (profile == 1) {
            admin.prop("disabled", "");
            admin.css({
                "cursor": "pointer"
            });
        }
        else if (profile == 2) {
            admin.prop("disabled", "disabled");
            admin.css({
                "cursor": "not-allowed"
            });
        }
        
        $scope.getUsers();
        metryc = new Metryc();
        metryc.setConnection('ws://localhost:9000/Metryc/feat');
        var request9 = {
            addtogroup: [id, window.localStorage.getItem("id")]
        }
        var request8 = {
            getfromgroup: [id, window.localStorage.getItem("id")]
            , request: {
                high: {
                    package: 'jmlv.org.Metryc.Editor'
                    , method: 'readDocument(String, Integer)'
                    , data: [route, id]
                    , callback: 'refresh'
                }
            }
        }
        metryc.sendMessage(JSON.stringify(request9));
        metryc.sendMessage(JSON.stringify(request8));
        editor = CodeMirror.fromTextArea(document.getElementById('testArea'), {
            lineNumbers: true
        });
        //        console.log(editor);
        ////        function sendText(editor, route, id){
        //        	var init = true;
        //        	var from, to
        ////        	 setTimeout(function(){ 
        ////        		 console.log(from);
        ////        		 console.log(to);
        ////        		 console.log(editor.getRange(from, to));
        ////        	     editor.toTextArea();
        //////        		 var request8 = {
        //////        		            getfromgroup: id,
        //////        		            request: {
        //////        		                high: {
        //////        		                    package: 'jmlv.org.Metryc.Editor',
        //////        		                    method: 'writeDocument(String, String, Integer, Integer)',
        //////        		                    data: [route, editor.getRange(from, to), from.line, id ],
        //////        		                    callback: 'funcion'
        //////        		                }
        //////
        //////        		            }
        //////        		        }
        //////        		        metryc.sendMessage(JSON.stringify(request8));  
        ////        		 alert(editor.getRange(from, to));
        ////        		 }, 2000);
        //        	
        ////        }
    }
    plasmarN(); // inicio de sesion
    //click en el boton gdt
    $scope.showadminbox = () => {
            var adminbox = document.getElementById('adminbox');
            console.log(adminbox.style.display);
            if (adminbox.style.display == "block") {
                bootbox.alert({
                    message: "El diálogo de administración del grupo de trabajo está abierto."
                    , className: 'bb-alternate-modal'
                });
            }
            else {
                $('#adminbox').css({
                    "display": "block"
                });
            }
        }
        //cerrar el adminbox
    xgdt.click(() => {
        $('#adminbox').css({
            "display": "none"
        });
        $('#adminadd').css({
            "display": "none"
        });
        $('#admindelete').css({
            "display": "none"
        });
        document.getElementById('adminemail').innerHTML = "";
        document.getElementById('addinput').value = "";
    });
    // crear archivo
    $scope.create = () => {
        var titulo = dname.val().split(' ').join('_'), // nombre del archivo a crear
            vacio = "";
        if (titulo != "") {
            request5 = {
                low: {
                    package: 'jmlv.org.Metryc.Desktop'
                    , method: 'newDocument(String, Integer)'
                    , data: [titulo, window.localStorage.getItem("id")]
                }
            }
            $.ajax({
                url: "./Feat"
                , type: "POST"
                , dataType: "json"
                , data: {
                    request: JSON.stringify(request5)
                }
                , success: data => {
                    console.log(data.response);
                    data = data.response.result;
                    if (data == 0) {
                        dname.css({
                            "border": "1px solid #d9534f"
                        });
                        alertdoc1.css({
                            "display": "block"
                        });
                        alertdoc2.css({
                            "display": "none"
                        });
                        document.getElementById("editor-dname").value = vacio;
                        editor1.css({
                            "display": "none"
                        });
                        escritorio1.css({
                            "display": "block"
                        });
                        plasmarN();
                    }
                    if (data == 1) {
                        dname.css({
                            "border": "1px solid #cccccc"
                        });
                        alertdoc1.css({
                            "display": "none"
                        });
                        alertdoc2.css({
                            "display": "none"
                        });
                        document.getElementById("editor-dname").value = vacio;
                        $scope.loadDocuments();
                    }
                }
                , error: err => {
                    console.log(err); // err
                }
            });
        }
        else {
            dname.css({
                "border": "1px solid #d9534f"
            });
            alertdoc1.css({
                "display": "none"
            });
            alertdoc2.css({
                "display": "block"
            });
        };
    }
    $scope.addUser = () => {
        var iddoc = document.getElementById("gdt").getAttribute("name");
        request8 = {
            low: {
                package: 'jmlv.org.Metryc.Editor'
                , method: 'addUser(Integer, String)'
                , data: [iddoc, document.getElementById('adminemail').innerHTML]
            }
        }
        $.ajax({
            url: "./Feat"
            , type: "POST"
            , dataType: "json"
            , data: {
                request: JSON.stringify(request8)
            }
            , success: data => {
                data = data.response.result;
                if (data == 1) {
                    console.log("anadido");
                    $('#adminadd').css({
                        "display": "none"
                    });
                    $('#admindelete').css({
                        "display": "none"
                    });
                    document.getElementById('adminemail').innerHTML = "";
                    document.getElementById('addinput').value = "";
                    $scope.getUsers();
                }
                else {
                    bootbox.alert("El email corresponde al de alguno de los participantes del grupo de trabajo.");
                }
            }
            , error: err => {
                console.log(err); // err
            }
        });
    }
    $scope.removeUser = () => {
        var iddoc = document.getElementById("gdt").getAttribute("name");
        request8 = {
            low: {
                package: 'jmlv.org.Metryc.Editor'
                , method: 'removeUser(Integer, String)'
                , data: [iddoc, document.getElementById('adminemail').innerHTML]
            }
        }
        $.ajax({
            url: "./Feat"
            , type: "POST"
            , dataType: "json"
            , data: {
                request: JSON.stringify(request8)
            }
            , success: data => {
                data = data.response.result;
                if (data == 1) {
                    console.log("eliminado");
                    $('#adminadd').css({
                        "display": "none"
                    });
                    $('#admindelete').css({
                        "display": "none"
                    });
                    document.getElementById('adminemail').innerHTML = "";
                    document.getElementById('addinput').value = "";
                    $scope.getUsers();
                }
                else {
                    bootbox.alert("Este usuario no puede ser eliminado, ya que no forma parte del grupo de trabajo.");
                }
            }
            , error: err => {
                console.log(err); // err
            }
        });
    }
    $scope.searchUser = () => {
        var inpval = document.getElementById('addinput').value.toLowerCase()
            , pattemailu = new RegExp("^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$");
        if (inpval != "") {
            if (pattemailu.test(inpval.trim()) == true) {
                if (inpval.trim() != window.localStorage.getItem('email')) {
                    request8 = {
                        low: {
                            package: 'jmlv.org.Metryc.SignUp'
                            , method: 'CheckEmail(String)'
                            , data: [inpval]
                        }
                    }
                    $.ajax({
                        url: "./Feat"
                        , type: "POST"
                        , dataType: "json"
                        , data: {
                            request: JSON.stringify(request8)
                        }
                        , success: data => {
                            data = data.response.email[0].count;
                            if (data == 0) {
                                inpval = "";
                                document.getElementById('adminemail').innerHTML = "El email ingresado no está registrado.";
                                $('#adminadd').css({
                                    "display": "none"
                                });
                                $('#admindelete').css({
                                    "display": "none"
                                });
                                document.getElementById('addinput').value = "";
                            }
                            if (data == 1) {
                                console.log("si existe el email");
                                document.getElementById('adminemail').innerHTML = inpval;
                                inpval = "";
                                $('#adminadd').css({
                                    "display": "block"
                                });
                                $('#admindelete').css({
                                    "display": "block"
                                });
                                document.getElementById('addinput').value = "";
                            }
                        }
                        , error: err => {
                            console.log(err); // err
                        }
                    });
                }
                else {
                    bootbox.alert("Usted es el administrador del grupo de trabajo, por lo que ya forma parte del mismo.");
                }
            }
            else { //hasta aca
                bootbox.alert("Ingrese un email válido.");
            }
        }
        else { //hasta aca
            bootbox.alert("Ingrese el email del usuario que desea agregar como nuevo participante en el grupo de trabajo.");
        }
    }
    $scope.signout = function () {
        window.localStorage.removeItem("id");
        window.localStorage.removeItem("name");
        window.localStorage.removeItem("lastname");
        $state.go("home");
    }
    $scope.delete = function (id, name) {
        request8 = {
            low: {
                package: 'jmlv.org.Metryc.Desktop'
                , method: 'deleteDocument(Integer, Integer)'
                , data: [id, window.localStorage.getItem("id")]
            }
        }
        $.ajax({
            url: "./Feat"
            , type: "POST"
            , dataType: "json"
            , data: {
                request: JSON.stringify(request8)
            }
            , success: data => {
                data = data.response.result;
                if (data == 0) {
                    $scope.loadDocuments();
                }
                if (data == 1) {
                    $scope.loadDocuments();
                    bootbox.alert("This is the default alert!");
                }
            }
            , error: err => {
                console.log(err); // err
            }
        });
    }
    $scope.titlefix = function (title) {
        var t = title;
        if (title.length > 9) {
            t = title.slice(0, 9) + "...";
        }
        return t;
    }
    $scope.emailfix = function (email) {
        var t = email;
        if (email.length > 30) {
            t = email.slice(0, 30) + "...";
        }
        return t;
    }
    $scope.setAdm = function (id) {
        var t = "";
        if (id == 1) {
            t = "Adm";
        }
        return t;
    }
    $scope.change = function (id, name) {
        var titledocument = $("#title-document" + id)
            , titlechange = $("#title-change" + id);
        titledocument.css({
            "display": "none"
        });
        titlechange.css({
            "display": "block"
        });
        document.getElementById("title-change" + id).onkeypress = (event) => {
            if (event.which == 13) {
                if (document.getElementById("title-change" + id).value != "") {
                    var oldtitle = document.getElementById("title-document" + id).innerHTML
                        , newtitle = document.getElementById("title-change" + id).value.split(' ').join('_');
                    request7 = {
                        low: {
                            package: 'jmlv.org.Metryc.Desktop'
                            , method: 'changeDocument(String, String, Integer, Integer)'
                            , data: [newtitle, name, id, window.localStorage.getItem("id")]
                        }
                    }
                    $.ajax({
                        url: "./Feat"
                        , type: "POST"
                        , dataType: "json"
                        , data: {
                            request: JSON.stringify(request7)
                        }
                        , success: data => {
                            data = data.response.result;
                            if (data == 0) {
                                bootbox.alert("Ya existe un archivo de texto con ese nombre. Intente otro nombre.");
                                titledocument.css({
                                    "display": "block"
                                });
                                titlechange.css({
                                    "display": "none"
                                });
                                document.getElementById("title-change" + id).value = "";
                            }
                            if (data == 1) {
                                alertdoch2.css({
                                    "display": "none"
                                });
                                titledocument.css({
                                    "display": "block"
                                });
                                if (newtitle.length > 9) {
                                    newtitle = newtitle.slice(0, 8) + "...";
                                    document.getElementById("title-document" + id).innerHTML = newtitle;
                                    document.getElementById("title-change" + id).value = "";
                                    titlechange.css({
                                        "display": "none"
                                    });
                                }
                                else {
                                    document.getElementById("title-document" + id).innerHTML = newtitle;
                                    document.getElementById("title-change" + id).value = "";
                                    titlechange.css({
                                        "display": "none"
                                    });
                                }
                                $scope.loadDocuments();
                            }
                        }
                        , error: err => {
                            console.log(err); // err             
                        }
                    });
                }
                else {
                    bootbox.alert("Intruzca el nombre por el cual desea cambiar el nombre del archivo.");
                }
            }
        }
    };
    $scope.open = function (id, route, name, profile) {
        escritorio1.css({
            "display": "none"
        });
        editor1.css({
            "display": "block"
        });
        plasmarNyT(id, route, name, profile);
    }
    xgdt.click(() => {});
    // funcion del row editor para cambiar al escritorio
    cambiesc.click(() => {
        bootbox.confirm({
            size: "small"
            , message: "¿Desea salir del editor?"
            , buttons: {
                confirm: {
                    label: 'Ok'
                    , className: 'btn-success'
                }
                , cancel: {
                    label: 'Cancelar'
                    , className: 'btn-danger'
                }
            }
            , callback: (result) => {
                if (result == true) {
                    bootbox.confirm({
                        size: "small"
                        , message: "Antes de salir del editor, ¿desea guardar los cambios?"
                        , buttons: {
                            confirm: {
                                label: 'Ok'
                                , className: 'btn-success'
                            }
                            , cancel: {
                                label: 'Cancelar'
                                , className: 'btn-danger'
                            }
                        }
                        , callback: (result) => {
                            if (result == true) {
                                alert("guardar cambios");
                                editor1.css({
                                    "display": "none"
                                });
                                escritorio1.css({
                                    "display": "block"
                                });
                                $('#adminbox').css({
                                    "display": "none"
                                });
                                $('#adminadd').css({
                                    "display": "none"
                                });
                                $('#admindelete').css({
                                    "display": "none"
                                });
                                document.getElementById('adminemail').innerHTML = "";
                                document.getElementById('addinput').value = "";
                                plasmarN();
                            }
                            else {
                                alert("no guardar cambios");
                                editor1.css({
                                    "display": "none"
                                });
                                escritorio1.css({
                                    "display": "block"
                                });
                                $('#adminbox').css({
                                    "display": "none"
                                });
                                $('#adminadd').css({
                                    "display": "none"
                                });
                                $('#admindelete').css({
                                    "display": "none"
                                });
                                document.getElementById('adminemail').innerHTML = "";
                                document.getElementById('addinput').value = "";
                                plasmarN();
                            }
                        }
                    });
                }
                else {
                    editor1.css({
                        "display": "block"
                    });
                    escritorio1.css({
                        "display": "none"
                    });
                }
            }
        });
        editor.toTextArea();
        metryc.closeConnection();
        console.log('borrado');
        document.getElementById("testArea").value = "";
        console.log(editor);
    });
    //boton de descarga de archivo en el editor
    botontxt.click(() => {
        var request8 = {
            getfromgroup: [id, window.localStorage.getItem("id")]
            , request: {
                high: {
                    package: 'jmlv.org.Metryc.Editor'
                    , method: 'readDocument(String, Integer)'
                    , data: [route, id]
                    , callback: 'dsave'
                }
            }
        }
        metryc.sendMessage(JSON.stringify(request8));
    });
                    }]);