(function () {
    // инициализация страницы
    jQuery(function () {
        refreshServerList();
    });
})();

//======================================================
// загрузить список серверов
//======================================================

function refreshServerList() {
    $.ajax({
        type: 'POST',
        url: 'loadServers',
        success: function (responseText) {
            removeServers(responseText);
            fillTable(responseText);
        }
    });
}

function fillTable(json) {
    var JSONservers = JSON.parse(json);
    var table = $('#serverTable');
    for (var i = 0; i < JSONservers.servers.length; i++) {
        var server = JSONservers.servers[i];
        $('#serverTable').append(($('<tr></tr>')).attr('id', 'row' + i));
        $('#row' + i).append(($('<td></td>')).html(server.serverName));
        if (server.isActive == true)
            $('#row' + i).append(($('<td></td>')).attr('id', 'active' + i).html("Сервер работает исправно.").addClass('work'));
        else
            $('#row' + i).append(($('<td></td>')).attr('id', 'active' + i).html("Сервер не работает!").addClass('not_work'));
        $('#row' + i).append(($('<td></td>')).html(new Date(server.lastCheckTime)));
        if (server.lastSuccessCheckTime != null)
            $('#row' + i).append(($('<td></td>')).html(new Date(server.lastSuccessCheckTime)));
        else
            $('#row' + i).append(($('<td></td>')).html("Это первая проверка!"));
        $('#row' + i).append(($('<td></td>')).html(server.statusDescription));
        if (server.serverStopTime != null)
            $('#row' + i).append(($('<td></td>')).attr('id', 'stopTime' + i).html(new Date(server.serverStopTime)));
        else {
            $('#row' + i).append(($('<td></td>')).attr('id', 'stopTime' + i).html("Сервер работает."));
        }
        if (server.isCheckError == true) {
            $('#row' + i).append(($('<td></td>')).html("Ошибка проверки!").addClass('not_work'));
            $('#active' +i).empty().html("Ошибка при проверке сервера!").addClass('maybe_work');
            $('#stopTime' +i).empty();
            $('#row' + i).append(($('<td></td>')).html(server.checkErrorDescription));
        }
        else{
            $('#row' + i).append(($('<td></td>')).html("Нет ошибок проверки.").addClass('work'));
        }

    }
}


function removeServers(json) {
    var JSONservers = JSON.parse(json);
    for (var i = 0; i < JSONservers.servers.length; i++) {
        $('#row' + i).remove();
    }
}

$(document).ready(function () {
    refreshServerList();
    setInterval('refreshServerList()', 30000);
});



