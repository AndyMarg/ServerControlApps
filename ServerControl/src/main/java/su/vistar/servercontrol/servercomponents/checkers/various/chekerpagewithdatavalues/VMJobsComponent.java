package su.vistar.servercontrol.servercomponents.checkers.various.chekerpagewithdatavalues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class VMJobsComponent extends ChekerDatesServerComponent {

    private LoggerManager log = new LoggerManager();
    //Конструктор для компонента с названием сервера 

    public VMJobsComponent(String serverName) {
        this.serverName = serverName;
    }

//=======================================================================================    
    /**
     * Получение условий проверки активности сервера.
     *
     * @return списка дат для проверки
     */
    @Override
    public List<Date> getDatesList() {
        List<Date> list = new LinkedList<>();
        //Вызовы исключений таймаута запроса и исключения соединения
        try {
            HttpGetConnection.GetHtmlPageResult htmlPageResult = HttpGetConnection.getHtmlPageAndStatus("http://vistar.su:8083/VMJobs/");
            this.htmlPage = htmlPageResult.htmlPage;
            this.status = htmlPageResult.httpStatus;
        } catch (TimeoutException e) {
            throw new ServerErrorException("Таймаут запроса!");
        } catch(ConnectionException e){
            throw new CheckFailureException(e,"Ошибка проверки!Ошибка соединения!Не возможно загрузить страницу!");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //Поиск необходимого условия проверки,а именно даты последней отправки данных на сервер(постоянно обновляемой) 
        //с помощью регулярного выражения "task\\D+done\\D+(\\d+-\\d+-\\d+\\D\\d+:\\d+:\\d+)" Результатом будет например:  task-done">DONE</td><td>2015-04-10 15:43:04,
        //m.group(1) вернет '(\\d+-\\d+-\\d+\\D\\d+:\\d+:\\d+)' в данном случае '2015-04-10 15:43:04'
        //получение даты будет проходить до тех пор пока m.find() находит шаблон в полученном коде
        try {
            Pattern pattern = Pattern.compile("task\\D+done\\D+(\\d+-\\d+-\\d+\\D\\d+:\\d+:\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher m = pattern.matcher(htmlPage);
            while (m.find()) {
                try {
                    //Приводим дату типа String к типу даты
                    String dateStr = m.group(1);
                    list.add(formatter.parse(dateStr));
                } catch (ParseException pe) {
                    log.severe(pe, "Ошибка парсинга объекта SimpleDateFormat!");
                }
            }
        } catch (Exception e) {
             log.severe(e,"Не найдено совпадение с шаблоном регулярного выражения 'task\\D+done\\D+(\\d+-\\d+-\\d+\\D\\d+:\\d+:\\d+)'.");
        }

        return list;
    }

}
