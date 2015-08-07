package su.vistar.servercontrol.servercomponents.checkers.various.checkerpagewithstringvalues;

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

public class YandexForwardComponent extends CheckerValuesServerComponent {
    
    private static final LoggerManager log = new LoggerManager();
    //Конструктор для компонента с названием сервера 
    public YandexForwardComponent(String serverName) {
        this.serverName = serverName;
    }

//=======================================================================================    
    /**
     * Получение условия проверки активности сервера.
     *
     * @return значение поля "обработано записей"
     */
    @Override
    public int getCurrentValue() {
        List<String> list = new LinkedList<>();

        //Вызовы исключений таймаута запроса и исключения соединения
        try {
            HttpGetConnection.GetHtmlPageResult htmlPageResult = HttpGetConnection.getHtmlPageAndStatus("http://vps1.vistar.su:8082/YandexForward/");
            this.htmlPage = htmlPageResult.htmlPage;
            this.status = htmlPageResult.httpStatus;
        } catch (TimeoutException e) {
            throw new ServerErrorException("Таймаут запроса!");
        } catch(ConnectionException e){
            throw new CheckFailureException(e,"Ошибка проверки!Ошибка соединения!Не возможно загрузить страницу!");
        }
        //Поиск необходимого условия проверки,а именно число обработанных записей(постоянно обновляемое) 
        //с помощью регулярного выражения "Обработано записей\\D*(\\d+)" Результатом будет например: Обработано записей: 1908837,
        //m.group(1) вернет '(\\d+)' в данном случае '1908837'
        Integer recordsCount = 0;
        try {
            Pattern pattern = Pattern.compile("Обработано записей\\D*(\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher m = pattern.matcher(htmlPage);

            m.find();
            String recordsCountStr = m.group(1);
            recordsCount = Integer.parseInt(recordsCountStr);
        }
        catch(Exception e){
            log.severe(e,"Не найдено совпадение с шаблоном регулярного выражения 'Обработано записей\\D*(\\d+)'.");
        }

        return recordsCount;
    }

}
