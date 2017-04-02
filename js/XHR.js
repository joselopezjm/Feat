function XHR() {
    var nameObjH = null,
        nameMethH = null,
        structH = null,
        PdataH = null,
        tempNameObjH = null,
        tempNameMethH = null,
        nameObjL = null,
        nameMethL = null,
        structL = null,
        PdataL = null,
        tempNameObjL = null,
        tempNameMethL = null,
        Priority = null;

    var x = {
        data: {
            "high": {
                "nameObj": ' + getNameObjectH() + ',
                "nameMeth": ' + getNameMethodH() + ',
                "struct": [' + getStructH() + '],
                "data": [' + getDataH() + ']
            },
            "low": {
                "nameObj": ' + getNameObjectL() + ',
                "nameMeth": ' + getNameMethodL() + ',
                "struct": [' + getStructL() + '],
                "data": [' + getDataL() + ']
            }
        }
    }

    this.setPriority = priority => Priority = priority;
    this.setNameObject = nameobj => {
        if (Priority == 1)
            nameObjH = JSON.stringify(nameobj);
        if (Priority == 0)
            nameObjL = JSON.stringify(nameobj);
    };
    this.setNameMethod = namemeth => {
        if (Priority == 1)
            nameMethH = JSON.stringify(namemeth);
        if (Priority == 0)
            nameMethL = JSON.stringify(namemeth);

    };

    this.setCallbackFunction = function(funct) {}

    var getNameObjectH = () => {
        return nameObjH;
    }
    var getNameObjectL = () => {

        return nameObjL;
    }
    var getNameMethodH = () => {
        return nameMethH;
    }
    var getNameMethodL = () => {
        return nameMethL;
    }

    var addToStructH = tempstructH => {
        structH = (structH == null) ? "" + JSON.stringify(tempstructH) : structH + "," + JSON.stringify(tempstructH);
    }
    var addToStructL = tempstructL => {
        structL = (structL == null) ? "" + JSON.stringify(tempstructL) : structL + "," + JSON.stringify(tempstructL);
    }

    var addToDataH = (tempvalueH, temptypeH) => {
        PdataH = (temptypeH != null) ? (PdataH === null) ? "[" + tempvalueH + "," + temptypeH + "]" : PdataH + ",[" + tempvalueH + "," + temptypeH + "]" : (PdataH === null) ? "[" + tempvalueH + "]" : PdataH + ",[" + tempvalueH + "]";
    }

    var addToDataL = (tempvalueL, temptypeL) => {
        PdataL = (temptypeL != null) ? (PdataL === null) ? "[" + tempvalueL + "," + temptypeL + "]" : PdataL + ",[" + tempvalueL + "," + temptypeL + "]" : (PdataL === null) ? "[" + tempvalueL + "]" : PdataL + ",[" + tempvalueL + "]";
    }

    this.addParams = (value, type) => {
        if (typeof type === "string") { // var or array
            if (typeof value === "string" || typeof value === "number" || typeof value === "boolean") { // var
                if (Priority == 1) {
                    addToStructH("var");
                    addToDataH(JSON.stringify(value), JSON.stringify(type));
                }
                if (Priority == 0) {
                    addToStructL("var");
                    addToDataL(JSON.stringify(value), JSON.stringify(type));
                }

            }
            if (Array.isArray(value)) { // array
                if (Priority == 1) {
                    addToStructH("array");
                    addToDataH(JSON.stringify(value), JSON.stringify(type));
                }
                if (Priority == 0) {
                    addToStructL("array");
                    addToDataL(JSON.stringify(value), JSON.stringify(type));
                }
            }

        } else { // record or array_record
            if (type == null) { // array_record
                if (value.length > 1) {
                    if (Priority == 1) {
                        addToStructH("array_record");
                        addToDataH(JSON.stringify(value));
                    }
                    if (Priority == 0) {
                        addToStructL("array_record");
                        addToDataL(JSON.stringify(value));
                    }
                }
            } else {
                if (Array.isArray(type)) { // record
                    if (Priority == 1) {
                        addToStructH("record");
                        addToDataH(JSON.stringify(value), JSON.stringify(type));
                    }
                    if (Priority == 0) {
                        addToStructL("record");
                        addToDataL(JSON.stringify(value), JSON.stringify(type));
                    }
                }
            }
        }
    };

    var getStructH = () => {
        return structH;
    }
    var getStructL = () => {
        return structL;
    }
    var getDataH = () => {
        return PdataH;
    }
    var getDataL = () => {
        return PdataL;
    }

    var getJson = () => {
        var jsn = '';
        if (nameObjL == null ||
            nameMethL == null) {
            var data = getDataH();
            var struct = getStructH();
            if (data == null)
                data = "";
            if (struct == null)
                struct = "";
            jsn = 'JsonRequest={"request":{"high":{"nameObj":' + getNameObjectH() + ',"nameMeth":' + getNameMethodH() + ',"struct":[' + struct + '],"data":[' + data + ']},"low":{"nameObj":"","nameMeth":"","struct":[],"data":[]}}}';
        } else if (nameObjH == null ||
            nameMethH == null) {
            var data = getDataL();
            var struct = getStructL();
            if (data == null)
                data = "";
            if (struct == null)
                struct = "";
            jsn = 'JsonRequest={"request":{"high":{"nameObj":"","nameMeth":"","struct":[],"data":[]},"low":{"nameObj":' + getNameObjectL() + ',"nameMeth":' + getNameMethodL() + ',"struct":[' + struct + '],"data":[' + data + ']}}}';
        } else {
            var dataH = getDataH();
            var structH = getStructH();
            if (dataH == null)
                dataH = "";
            if (structH == null)
                structH = "";
            var dataL = getDataL();
            var structL = getStructL();
            if (dataL == null)
                dataL = "";
            if (structL == null)
                structL = "";
            jsn = 'JsonRequest={"request":{"high":{"nameObj":' + getNameObjectH() + ',"nameMeth":' + getNameMethodH() + ',"struct":[' + structH + '],"data":[' + dataH + ']},"low":{"nameObj":' + getNameObjectL() + ',"nameMeth":' + getNameMethodL() + ',"struct":[' + structL + '],"data":[' + dataL + ']}}}';

        }
        console.log(jsn);
        return jsn;
    }

    var clearJson = () => {
        nameObjH = null,
            nameMethH = null,
            structH = null,
            PdataH = null,
            tempNameObjH = null,
            tempNameMethH = null,
            nameObjL = null,
            nameMethL = null,
            structL = null,
            PdataL = null,
            tempNameObjL = null,
            tempNameMethL = null,
            Priority = null;
    }

    var xmlObject = null;
    var data = null;
    var err = null;
    this.getData = function() {
        return data
    };
    var xhr = (method, url, params, callback) => {
        try {
            xmlObject = new XMLHttpRequest();
            xmlObject.onreadystatechange = () => {
                if (xmlObject.status === 200 && xmlObject.readyState === 4) {
                    data = (xmlObject.responseText);
                    data = JSON.parse(data);
                    callFunct();
                }
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
    this.get = (url, params, callback) => {
        callFunct = new Function(callback);
        params = JSON.stringify(params).replace(/\s/g, '');
        if (params == "{}") {
            if (nameObjH == null && nameObjL == null) {
                url += "?" + params;
                xhr("get", url, params, callback);
                clearJson();
            } else {
                url += "?" + getJson();
                params = getJson();
                xhr("get", url, params, callback);
                clearJson();
            }
        } else {
            params = params.replace(/"/g, "");
            url += "?" + params;
            xhr("get", url, params, callback);
            clearJson();
        }
    }

    this.post = function(url, params, callback) {
        callFunct = new Function(callback);
        params = JSON.stringify(params).replace(/\s/g, '');
        if (params == "{}") {
            if (nameObjH == null && nameObjL == null) {
                xhr("post", url, params, callback);
                clearJson();
            } else {
                params = getJson();
                xhr("post", url, params, callback);
                clearJson();
            }
        } else {
            params = params.replace(/"/g, "");
            xhr("post", url, params, callback);
            clearJson();
        }
    }
}