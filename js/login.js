   var $http = new XHR();
   function login() {
       $http.setNameObject("jmlv.org.ExampleXHR.Login");
       $http.setNameMethod("login"); $http.addParams([document.getElementById('inputnnlogin').value, document.getElementById('inputplogin').value], 'string');
       $http.post("./test", {}, 'login2()');
   }

   function login2() {
       var data = $http.getData();
       if (data.status=="true") {
        document.getElementById('login').style.display = "none";
           document.getElementById('request').style.display = "block";
       } else {
           alert(data.response);
       }
   }