package su.vistar.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import su.vistar.DatabaseServlet;
import su.vistar.db.DatabaseConnection;
import su.vistar.db.entity.CountEntity;
import su.vistar.db.entity.GetMessageEntity;
import su.vistar.db.entity.QuoteEntity;
import su.vistar.db.mapper.ApplicationMapper;
import su.vistar.db.mapper.MessageMapper;
import su.vistar.db.mapper.QuoteMapper;
import su.vistar.db.mapper.SenderMapper;
import su.vistar.logging.LoggerManager;

public class AddMessage implements Action {

    public static final LoggerManager log = new LoggerManager();

//=================================================================================================
    /**
     * Получает id отправителя по названию предприятия.
     *
     * @param name имя предприятия или отправителя
     * @return id отправителя
     */
    public Integer getSenderId(String name) {
        Integer senderId = null;
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            try {
                SenderMapper mapp = session.getMapper(SenderMapper.class);
                senderId = mapp.getSenderIdByName(name);
            } catch (Exception e) {
                log.severe(e, "Ошибка получения senderId!");
            }

        }
        return senderId;
    }

//=====================================================================================================
    /**
     * Получение id приложения,при достверности отправленого логина и пароля
     *
     * @param appName логин(название приложения)
     * @param appKey пароль для соответсвующего приложения
     * @return
     */
    public Integer getApplicationId(String appName, String appKey) {
        Integer appId = null;
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            try {
                ApplicationMapper mapp = session.getMapper(ApplicationMapper.class);
                appId = mapp.getApplicationIdByLoginAndKey(appName, appKey);
            } catch (Exception e) {
                log.severe(e, "Ошибка получения appId!");
            }
        }

        return appId;
    }

//=======================================================================================================
    /**
     * Получает количество сообщений каждого типа(различаются по статусу
     * отправки) для конкретного приложения за последний час.
     *
     * @param msgAppId id приложения для которого готовится список количества
     * сообщений каждого типа
     * @return список,каждый элемент которого количество сообщений определенного
     * типа
     */
    public List<CountEntity> getStatusesCount(int msgAppId) {
        List<CountEntity> statues = new ArrayList<>();
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            try {
                MessageMapper mapp = session.getMapper(MessageMapper.class);
                statues = mapp.getStatusesCountByAppId(msgAppId);
            } catch (Exception e) {
                log.severe(e, "Ошибка получения количества сообщений с каждым статусом за последний час для приложения с id = " + msgAppId);
            }
        }

        return statues;
    }

//============================================================================================================
    /**
     * Возвращает количество сообщений для конкретного приложения со статусом
     * "SEND_TO_PHONE" и "NOT_SEND"
     *
     * @param statues количество сообщений каждого типа (различаются по статусу
     * отправки)
     * @return количество сообщений для конкретного приложения со статусом
     * "SEND_TO_PHONE" и "NOT_SEND"
     */
    public Integer getMessagesCountForQuote(List<CountEntity> statues) {
        Integer count = 0;
        for (Iterator<CountEntity> iterator = statues.iterator(); iterator.hasNext();) {

            CountEntity next = iterator.next();
            String status = next.getMsg_status();
            if (("SEND_TO_PHONE".equals(status)) || ("NOT_SEND".equals(status))) {
                int cnt = next.getCnt();
                count += cnt;
            }
        }

        return count;
    }

//=============================================================================================================
    /**
     * Получает квоты для конкретного приложения.
     *
     * @param appId id приложения для которого ищутся квоты
     * @return объект QuoteEntity со всеми квотами для исходного приложения
     */
    public QuoteEntity getQuotes(int appId) {

        QuoteEntity quoteEntity = null;
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            QuoteMapper mapp = session.getMapper(QuoteMapper.class);
            quoteEntity = mapp.getQuotesByAppId(appId);
            if (quoteEntity == null) {
                throw new RuntimeException("Не удалось получить квоты для приложения с id = " + appId);
            }
        }

        return quoteEntity;
    }

//==============================================================================================================
    /**
     * Получает количество сообщений с текстом совпадающим с заданным в
     * параметре, для конкретного приложения,добавленных за интервал,также
     * заданный в параметре.
     *
     * @param text текст сообщения для сравнения
     * @param msgAppId id приложения
     * @param interval интервал времени для сравнения
     * @return
     */
    public int getMessagesWithSameTextCount(String text, int msgAppId, int interval) {
        Integer count = null;
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            try {
                MessageMapper mapp = session.getMapper(MessageMapper.class);
                count = mapp.getSameMessageCount(text, msgAppId, interval);
            } catch (Exception e) {
                log.severe(e, "Ошибка поучения количества одинаковых сообщений для приложения с id = " + msgAppId);
            }
        }

        return count;
    }

 //==================================================================================================================
    /**
     * Возвращает статус добавления сообщения в БД.
     *
     * @param messageText текст сообщения
     * @param fromNumbers номера адресатов
     * @param senderId id отправителя
     * @param senderName имя отправителя или название компании
     * @param sendStatus статус отправки сообщения
     * @param comment комментарий к сообщению(тема)
     * @param creationDate дата добавления сообщения в БД
     * @param sendToPhoneDate дата отправки на устройство
     * @param sendDate дата отправки сообщения
     * @param appId id приложения отк оторого пришло сообщение
     * @return true,если сообщение удачно добавлено в базу и false если есть
     * ошибка добавления
     */
    public boolean addMessage(String messageText, String fromNumbers, int senderId, String senderName, String sendStatus, String comment, Date creationDate, Date sendToPhoneDate, Date sendDate, int appId) {
        boolean status;
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            try {
                MessageMapper mapp = session.getMapper(MessageMapper.class);
                mapp.addMessageSettings(messageText, fromNumbers, senderId, senderName, sendStatus, comment, creationDate, sendToPhoneDate, sendDate, appId);
                status = true;
            } catch (Exception e) {
                status = false;
                log.severe(e, "Ошибка добавления нового сообщения в базу!");
            }

            session.commit();

        }

        return status;
    }

//==================================================================================================================
    /**
     * возвращает объект json для отправки ответа устройству,отправившему
     * запрос.
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public JsonObject execute(HttpServletRequest request, HttpServletResponse response) {

        GetMessageEntity getMessageEntity = null;

        HashMap<String, String> params = DatabaseServlet.getParams();
        ObjectMapper mapper = new ObjectMapper();
        final JsonObject obj = new JsonObject();
        final Gson gson = new Gson();
        String state;
        boolean status = false;

        try {
            //Получаем строку json из запроса
            getMessageEntity = mapper.readValue(params.get("json"), GetMessageEntity.class);
            //из строки получаем текст сообщения
            String msg_text = getMessageEntity.getMsg_text();
            //номера адресатов
            String msg_phone_numbers = getMessageEntity.getMsg_phone_numbers();
            //Имя отправителя или название компании
            String sd_name = getMessageEntity.getSd_name();
            //Получаем id отправителя по имени
            Integer msg_sender_id = getSenderId(sd_name);
            //Проверка на существование такого имени отправителя в БД
            if (msg_sender_id == null) {
                throw new RuntimeException("В базе еще нет такого значения поля sd_name: " + sd_name);
            }
            //получаем тему сообщения
            String msg_comment = getMessageEntity.getMsg_comment();
            //создаем текущую дат для добавления сообщения в БД
            Date msg_creation_date = new Date();
            //Получаем id приложения через логин и пароль
            String app_name = getMessageEntity.getApp_name();
            String app_key = getMessageEntity.getApp_key();
            Integer msg_app_id = getApplicationId(app_name, app_key);
            //проверка на подлиность логина и пароля
            if (msg_app_id == null) {
                throw new RuntimeException("В базе нет такого логина app_name: " + app_name + " или неверный пароль!");
            }
            //Получение квот для конкретного приложения
            QuoteEntity quoteEntity = getQuotes(msg_app_id);
            //Получаем максимально количество сообщений от одного приложения за час
            Integer maxMessageCount = quoteEntity.getQt_max_per_hour();
            //интервал для ограничения на одинаковые сообщения
            Integer interval = quoteEntity.getQt_same_min_interval();

            //Полчаем количество сообщений каждого типа(различаются по статусу отправки) для конкретного приложения за последний час.
            List<CountEntity> statuses = getStatusesCount(msg_app_id);
            //Полчаем количество сообщений типа NOT SEND и SEND TO PHONE для конкретного приложения за последний час
            Integer count = getMessagesCountForQuote(statuses);
            //получаем количсетво сообщений с одинаковым текстом,добавленных за interval
            Integer sameMessageCount = getMessagesWithSameTextCount(msg_text, msg_app_id, interval);
            //проверка на ограничения квотами
            if (count >= maxMessageCount) {
                state = "QUOTE(MAX_COUNT)_EXCEED";
            } else if (sameMessageCount != 0) {
                state = "QUOTE(SAME_MESSAGE)_EXCEED";
            } else {
                state = "NOT_SEND";
            }
            //Добавление сообщения в Бд и получение успешного или неудачного статуса
            status = addMessage(msg_text, msg_phone_numbers, msg_sender_id, sd_name, state, msg_comment, msg_creation_date, null, null, msg_app_id);
        } catch (IOException e) {
            status = false;
            log.severe(e, "Ошибка в получении строки JSON для объекта GetMessageEntity!");
        } catch (RuntimeException re) {
            status = false;
            log.severe(re);
        }

        obj.addProperty("status", gson.toJson(status));
        return obj;
    }

}
