<%@page import="su.vistar.marshrut.data.client.version.Version"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Версия ServerControl</title>
    </head>
    <body>
        <h1>ServerControl</h1>
        <p>Description</p>

        <b>Не обновляет настройки из базы данных на лету! Требуется перезагрузка приложения!</b>
        <br>
        <br>
        
        <h4>Версия: <%= Version.getVersion()%></h4>
        <h4>Дата сборки: <%= Version.getBuildTime()%></h4>
        <h4>Коммит: <%= Version.getCommitNumber()%></h4>
    </body>
</html>
