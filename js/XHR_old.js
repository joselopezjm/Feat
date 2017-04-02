function XHR() {
    var nameObj = null,
        nameMeth = null,
        struct = null,
        Pdata = null,
        tempNameObj = null,
        tempNameMeth = null,
        sendData = 'jdata={"nameObj":' + nameObj + ',"nameMeth":' + nameMeth + ',"struct":[' + struct + '],"data":[' + Pdata + ']}';


    function getSendData() {
        sendData = 'jdata={"nameObj":' + getNameObject() + ',"nameMeth":' + getNameMethod() + ',"struct":[' + getStruct() + '],"data":[' + getData() + ']}';
        return sendData;
    }

    this.setNameObject = function (nameobj) {
        nameObj = JSON.stringify(nameobj);

    }
     function getNameObject () {
        return nameObj;
    }
    this.setNameMethod = function (namemeth) {
        nameMeth = JSON.stringify(namemeth);
    }
    function getNameMethod () {
        return nameMeth
    }

    function addToStruct(tempstruct) {
        if (struct == null) {
            struct = "" + JSON.stringify(tempstruct);
        } else {
            struct = struct + "," + JSON.stringify(tempstruct);

        }
    }

    function addToData(tempvalue, temptype) {
        if (temptype != null) {
            if (Pdata === null) {
                Pdata = "[" + tempvalue + "," + temptype + "]";
            } else {
                Pdata = Pdata + ",[" + tempvalue + "," + temptype + "]";
            }
        } else {
            if (Pdata === null) {
                Pdata = "[" + tempvalue + "]";
            } else {
                Pdata = Pdata + ",[" + tempvalue + "]";
            }
        }
    }



    this.addParams = function (value, type) {
        if (typeof type === "string") { //var or array
            if (typeof value === "string" || typeof value === "number" || typeof value === "boolean") { //var
                addToStruct("var");
                addToData(JSON.stringify(value),JSON.stringify(type));
            }

            if (Array.isArray(value)) { //array
                //if (value[0][0] === undefined) {
                    addToStruct("array");
                    addToData(JSON.stringify(value),JSON.stringify(type));
                //}
            }

        } else {//record or array_record
            
            if (type == null) {//array_record
                if (value.length > 1) {
                    addToStruct("array_record");
                    addToData(JSON.stringify(value));
                }
            } else {
                if (Array.isArray(type)) { //record
                    addToStruct("record");
                    addToData(JSON.stringify(value),JSON.stringify(type));
                }

            }


        }
    };

    function getStruct  () {
        return struct;
    }
    
    function getData  () {
        return Pdata;
    }

    //	var jsonToParams = function (json){
    //		var res = "";
    //		for (var attr in json){
    //			if(res===""){
    //				res = attr+'='+json[attr]
    //			}else{
    //				res+='&'+attr+'='+json[attr];
    //			}
    //		}
    //		return res;
    //	}
    var xmlObject = null;
    var data = null;
    var err = null;

    var xhr = function (method, url, params, callback) {
        try {
            xmlObject = new XMLHttpRequest();
            xmlObject.onreadystatechange = function () {
                if (xmlObject.status === 200 && xmlObject.readyState === 4) {
                    data = JSON.parse = (xmlObject.responseText);
                    callback(null, data)
                } //else{
                //err= xmlObject.status;
                //callback(err,null);
                //}
            }
            xmlObject.open(method, url, true);
            xmlObject.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            if (method === "post") {
                xmlObject.send(params);
            } else {
                xmlObject.send();
            }
        } catch (err) {
            callback(err, null)
        }
    }
    this.get = function (url, params, callback) {
        //		params =typeof params==="string"?
        //				params:jsonToParams(params);
        params = JSON.stringify(params).replace(/\s/g, '');
        if (params == "{}") {
            if (nameObj == null || nameMeth == null || struct == null || Pdata == null) {
                url += "?" + params;
                xhr("get", url, params, callback);
            } else {
                url += "?" + getSendData();
                xhr("get", url, getSendData(), callback);
            }
        } else {
            params = params.replace(/"/g, "");
            url += "?" + params;
            xhr("get", url, params, callback);
        }
    }

    this.post = function (url, params, callback) {
        //		params =typeof params==="string"?
        //				params:jsonToParams(params);
        if (params == "{}") {
            if (nameObj == null || nameMeth == null || struct == null || Pdata == null) {
                xhr("get", url, params, callback);
            } else {
                xhr("get", url, sendData, callback);
            }
        } else {
            params = params.replace(/"/g, "");
            xhr("get", url, params, callback);
        }
        console.log('post' + params);
    }

    this.put = function (url, params, callback) {
        //		params =typeof params==="string"?
        //				params:jsonToParams(params);
        if (params == "{}") {
            if (nameObj == null || nameMeth == null || struct == null || Pdata == null) {
                url += "?" + params;
                xhr("get", url, params, callback);
            } else {
                url += "?" + sendData;
                xhr("get", url, sendData, callback);
            }
        } else {
            params = params.replace(/"/g, "");
            url += "?" + params;
            xhr("get", url, params, callback);
        }
    }

    this.del = function (url, params, callback) {
        //		params =typeof params==="string"?
        //				params:jsonToParams(params);
        if (params == "{}") {
            if (nameObj == null || nameMeth == null || struct == null || Pdata == null) {
                url += "?" + params;
                xhr("get", url, params, callback);
            } else {
                url += "?" + sendData;
                xhr("get", url, sendData, callback);
            }
        } else {
            params = params.replace(/"/g, "");
            url += "?" + params;
            xhr("get", url, params, callback);
        }
    }
}