function sendPost() {

    $.ajax({
        type: 'POST',
        url: 'http://localhost:8084/DatabaseTest/DatabaseServletTest',
        data: "command=reqAddMessage&json=" + getJSON(),
        success: function (responseText) {
            writeStatusText(responseText);
        }
    });
}

function getJSON() {
    var JSONtest = {
        sd_name: "margashov",  
        app_name: "SmsGateWay",
        app_key: "1909",
        msg_phone_numbers: "89155828352,89515482000",
        msg_text: "PHP not bad)",
        msg_comment: "Тест"        
    };

    return JSON.stringify(JSONtest);
}

function writeStatusText(json) {
    var JSONgetStatus = JSON.parse(json);

    $('#getStatus').html(json);
}

function getPost() {

    $.ajax({
        type: 'POST',
        url: 'http://localhost:8084/DatabaseTest/DatabaseServletTest',
        data: "command=reqSendMessage&json=" + getJSONImei(),
        success: function (responseText) {
            writeText(responseText);
        }
    });
}

function writeText(json) {
    var JSONgetText = JSON.parse(json);

    $('#getText').html(json);
}


function sendStatus() {

    $.ajax({
        type: 'POST',
        url: 'http://localhost:8084/DatabaseTest/DatabaseServletTest',
        data: "command=reqCheckPassword&json=" + getJSONList(),
        success: function (responseText) {
            writeStatus(responseText);
        }
    });
}

function writeStatus(json) {
    var JSONgetText = JSON.parse(json);

    $('#getStatusText').html(json);
}

function getJSONList() {

    var JSONstatus = {
        sdp_imei: "000000000000000",
        sdp_password: "sosisa1909"
    };

   return JSON.stringify(JSONstatus);
}

function getJSONImei(){

        var JSONImei = {
        sdp_imei: "000000000000000",
        sdp_password: "sosisa190"
    };
    
    return JSON.stringify(JSONImei);
}


