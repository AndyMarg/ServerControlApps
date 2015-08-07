package su.vistar.servercontrol.servercomponents.checkers.various.gpsadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.entity.ConnectionEntity;
import su.vistar.servercontrol.entity.ConnectionsEntity;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.ConnectionException;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.exception.TimeoutException;
import su.vistar.servercontrol.httputils.HttpGetConnection;
import su.vistar.servercontrol.utils.ServiceLocator;

public class GPSAdapterComponent extends CheckerConnectionServerComponent {
    
    LoggerManager log = new LoggerManager();

    //Конструктор для компонента с названием сервера 
    public GPSAdapterComponent(String serverName) {
        this.serverName = serverName;
    }

//==========================================================================================================
    /**
     * Получение условия проверки активности сервера.
     *
     * @return условие проверки
     */
    @Override
    protected Map<String, ArrayList<String>> getConnectionList() {
        List<ConnectionEntity> connectionList = new LinkedList<>();
        try {
            //Проверка полученного списока подключений на пустоту и вызов соответствующего исключения
            connectionList = getConnections();
        } catch (ServerErrorException e) {
            this.statusDescription = e.statusDescription;
            throw new ServerErrorException(statusDescription);
        }

        Map<String, ArrayList<String>> map = new HashMap<>();
        //Заполнение hashMap для конкретного адаптера
        for (Iterator<ConnectionEntity> iterator = connectionList.iterator(); iterator.hasNext();) {
            ConnectionEntity connection = iterator.next();
            //Получение следующего имени адаптера 
            String adapterName = connection.getAdapterName();
            //Проверка hashMap на наличие ключа с таким именем
            //Если ключа нет
            if (map.containsKey(adapterName) == false) {
                //Создается массив подключенных устройств для данного ключа-адаптера
                ArrayList<String> channelState = new ArrayList<>();
                //Добавление значения
                channelState.add(connection.getChannelState());
                //Заполнение hashMap
                map.put(adapterName, channelState);
                //Если ключ есть
            } else {
                //Получение массива подключеных устройств  для этого ключа-адаптера из  hashMap
                ArrayList<String> channelState = map.get(adapterName);
                //Добавление значения
                channelState.add(connection.getChannelState());
                //Заполнение hashMap
                map.put(adapterName, channelState);
            }

        }
        return map;
    }

//==========================================================================================================
    /**
     * Получение списка подключений.
     *
     * @return список подключений в виде списка объектов ConnectionEntity
     */
    private List<ConnectionEntity> getConnections() {
        //Вызовы исключений таймаута запроса и исключения соединения
        try {
            HttpGetConnection.GetHtmlPageResult htmlPageResult = HttpGetConnection.getHtmlPageAndStatus("http://gps.vistar.su:8096/connections/list");
            this.htmlPage = htmlPageResult.htmlPage;
            this.status = htmlPageResult.httpStatus;
        } catch (TimeoutException e) {
            throw new ServerErrorException("Сервер " + this.serverName + " не работает!!Таймаут запроса!");
        }catch(ConnectionException e){
            throw new CheckFailureException(e,"Ошибка проверки!Ошибка соединения!Не возможно загрузить страницу!");
        }
        //Получение объекта ConnectionsEntity из вспомогательного компонента ServiceLocator
        ConnectionsEntity connectionsEntity = new ConnectionsEntity();
        ObjectMapper mapper = new ObjectMapper();
        try {
            //Получение объекта ConnectionsEntity из строки json
            connectionsEntity = mapper.readValue(htmlPage, ConnectionsEntity.class);
        } catch (IOException e) {
            log.severe(e,"Не удалось получить объект из строки json.");
        }
        //Получение списка подключений
        List<ConnectionEntity> connections = connectionsEntity.getConnections();
        //Вызов исключения ServerErrorException при получении пустого списка подключений
        if (connections == null) {
            throw new ServerErrorException("Не загружается список подключений!");
        }
        return connections;
    }

}
