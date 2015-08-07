package su.vistar.servercontrol.servercomponents.checkers.various.сhekerpagewithlongvalues;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import su.vistar.servercontrol.entity.BusArrivalEntity;
import su.vistar.servercontrol.entity.BusesEntity;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.ConnectionException;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.exception.TimeoutException;
import su.vistar.servercontrol.httputils.HttpPostConnection;

public class VPArrivalServerComponent extends ChekerTimeServerComponent {

//=====================================================================================
    //Конструктор для компонента с названием сервера 
    public VPArrivalServerComponent(String serverName) {
        this.serverName = serverName;
    }
//=====================================================================================   

    /**
     * Получение условий проверки активности сервера.
     *
     * @param routeName имя маршрута для которого надо полчить hashMap
     * @return hashMap с ключом в виде названия маршрута и значением - массивом
     * моментов времени до прибытия,относяшихся к этому маршруту
     * @throws java.lang.Exception
     */
    @Override
    protected Map<String, ArrayList<Integer>> getTimeList(String routeName) throws Exception {
        try {
            //Проверка полученного списока маршрутов на пустоту и вызов соответствующего исключения  
            checkRoutesList();
        } catch (ServerErrorException e) {
            this.statusDescription = e.statusDescription;
            throw new ServerErrorException(statusDescription);
        }

        HttpPostConnection.GetHtmlPageResult htmlPageResult;
        //Получение страницы и статуса  для первого маршрута
        //Проверка полученного списока данныхспо маршруту на пустоту и вызов соответствующего исключения
        try {
            if (routeName == "firstRoute") {
                htmlPageResult = getHtmlPageForRoute("http://passenger.vistar.su/VPArrivalServer/arrivaltimes", "{\"regionId\":\"36\",\"fromStopId\":\"3674\",\"toStopId\":\"3674\"}");
                this.htmlPage = htmlPageResult.htmlPage;
                this.status = htmlPageResult.httpStatus;
                //Аналогично для второго
            } else if (routeName == "secondRoute") {
                htmlPageResult = getHtmlPageForRoute("http://passenger.vistar.su/VPArrivalServer/arrivaltimes", "{\"regionId\":\"36\",\"fromStopId\":\"2863\",\"toStopId\":\"2863\"}");
                this.htmlPage = htmlPageResult.htmlPage;
                this.status = htmlPageResult.httpStatus;
            }
        } catch (ServerErrorException e) {
            this.statusDescription = e.statusDescription;
            throw new ServerErrorException(statusDescription);
        } 

        Map<String, ArrayList<Integer>> map = new HashMap<>();
        BusesEntity busesEntity = new BusesEntity();
        //Получение объекта BusesEntity из строки json
        ObjectMapper mapper = new ObjectMapper();

        busesEntity = mapper.readValue(htmlPage, BusesEntity.class);

        //Вызов исключения ServerErrorException при получении пустого списка для выбранного маршрута
        if (busesEntity == null) {
            throw new ServerErrorException("Не отображается список для выбранного маршрута!");
        }

        List<BusArrivalEntity> buses = busesEntity.getBusArrival();
        //Заполнение hashMap для конкретного маршрута
        for (Iterator<BusArrivalEntity> iterator = buses.iterator(); iterator.hasNext();) {
            BusArrivalEntity bus = iterator.next();
            //Получение следующего названия маршрута 
            String route = bus.getBusRoute();
            //Проверка hashMap на наличие ключа с таким именем
            //Если ключа нет
            if (map.containsKey(route) == false) {
                //Создается массив моментов времени для данного ключа-маршрута
                ArrayList<Integer> times = new ArrayList<>();
                //Добавление значения
                times.add(bus.getArrivalTime());
                //Заполнение hashMap
                map.put(route, times);
                //Если ключ есть
            } else {
                //Получение массива моментов времени  для этого ключа-маршрута из  hashMap
                ArrayList<Integer> times = map.get(route);
                //Добавление к массиву нового значения
                times.add(bus.getArrivalTime());
                //Заполнение hashMap
                map.put(route, times);
            }
        }

        return map;
    }
//==========================================================================================

    /**
     * Проверяет полученный список маршрутов на пустоту.
     */
    private void checkRoutesList() {

        try {
            HttpPostConnection.GetHtmlPageResult htmlPageResult = HttpPostConnection.getHtmlPageAndStatus("http://passenger.vistar.su/VPArrivalServer/routelist", "{\"regionId\":\"36\"}");
            this.htmlPage = htmlPageResult.htmlPage;
            this.status = htmlPageResult.httpStatus;
        } catch (TimeoutException e) {
            throw new ServerErrorException(e, "Таймаут запроса!");
        } catch (ConnectionException e) {
            throw new CheckFailureException(e, "Не найдена страница!");
        }
        //Поиск одного из условий проверки,а именно списка маршрутов(загружается при открытии страницы)
        //с помощью регулярного выражения "name\":\"([а-я]*[a-z]*[\\d]*[а-я]*)" Результатом будет например:  "name":"113кс",
        //m.group(1) вернет '([а-я]*[a-z]*[\\d]*[а-я]*)' в данном случае '113кс'
        //получение маршрута будет проходить до тех пор пока m.find() находит шаблон в полученном коде
        Pattern pattern = Pattern.compile("name\":\"([а-я]*[a-z]*[\\d]*[а-я]*)", Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(htmlPage);
        boolean routeList;

        routeList = m.find();
        if (routeList == false) {
            throw new ServerErrorException("Не отображается список маршрутов!");
        }

    }

//=============================================================================================
    /**
     * Получает объект GetHtmlPageResult для конкретного маршрута.
     *
     * @param url адрес ресурса по котором находится страница для получения
     * условий проверки
     * @param json параметры запроса в виде строки json
     * @return объект GetHtmlPageResult с полями htmlPage и httpStatus
     */
    private HttpPostConnection.GetHtmlPageResult getHtmlPageForRoute(String url, String json) {
        HttpPostConnection.GetHtmlPageResult htmlPageResult = new HttpPostConnection.GetHtmlPageResult();
        //Вызовы исключений таймаута запроса и исключения соединения
        try {
            htmlPageResult = HttpPostConnection.getHtmlPageAndStatus(url, json);
        } catch (TimeoutException e) {
            throw new ServerErrorException("Таймаут запроса!");
        } catch (ConnectionException e) {
            throw new CheckFailureException(e, "Ошибка проверки!Не возможно загрузить страницу!");
        }

        return htmlPageResult;
    }

}
