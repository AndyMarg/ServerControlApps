package su.vistar.servercontrol.servercomponents.checkers.various.chekerpagewithdatavalues;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.ConnectionException;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.exception.TimeoutException;
import su.vistar.servercontrol.httputils.HttpGetConnection;

public class CDSForwardComponent extends ChekerDatesServerComponent {

    private static final LoggerManager log = new LoggerManager();
    //Конструктор для компонента с названием сервера 
    public CDSForwardComponent(String serverName) {
        this.serverName = serverName;
    }
//=======================================================================================    

    /**
     * Получение условий проверки активности сервера.
     *
     * @return список дат для проверки
     */
    @Override
    protected List<Date> getDatesList() {
        List<Date> list = new LinkedList<>();
        //Вызовы исключений таймаута запроса и исключения соединения
        try {
            HttpGetConnection.GetHtmlPageResult htmlPageResult = HttpGetConnection.getHtmlPageAndStatus("http://vps2.vistar.su:8094/currentState");
            this.htmlPage = htmlPageResult.htmlPage;
            this.status = htmlPageResult.httpStatus;
        } catch (TimeoutException e) {
            throw new ServerErrorException("Таймаут запроса!");
        }catch(ConnectionException e){
            throw new CheckFailureException(e,"Ошибка проверки!Ошибка соединения!Не возможно загрузить страницу!");
        }
        //Поиск необходимого условия проверки,а именно даты последней отправки данных на сервер(постоянно обновляемой) в виде количества милисекунд 
        //с помощью регулярного выражения "cdsPkt2LastSendTime\\D+(\\d+)" Результатом будет например:  cdsPkt2LastSendTime":1428670136283,
        //m.group(1) вернет '(\\d+)' в данном случае '1428670136283'
        //получение даты будет проходить до тех пор пока m.find() находит шаблон в полученном коде
        try {
            Pattern pattern = Pattern.compile("cdsPkt2LastSendTime\\D+(\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher m = pattern.matcher(htmlPage);
            while (m.find()) {
                //Получаем количество миллисекунд и переводим его в дату
                long millis = Long.parseLong(m.group(1));
                list.add(new Date(millis));
            }
        } catch (Exception e) {
             log.severe(e,"Не найдено совпадение с шаблоном регулярного выражения 'cdsPkt2LastSendTime\\D+(\\d+)'.");
        }

        return list;

    }

}
