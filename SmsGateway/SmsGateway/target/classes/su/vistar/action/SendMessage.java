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
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.session.SqlSession;
import su.vistar.DatabaseServlet;
import su.vistar.util.ServiceLocator;
import su.vistar.db.CommonFunctions;
import su.vistar.db.DatabaseConnection;
import su.vistar.db.entity.GetImeiEntity;
import su.vistar.db.entity.GetPasswordEntity;
import su.vistar.db.entity.MessageEntity;
import su.vistar.db.entity.MessagesEntity;
import su.vistar.db.mapper.MessageMapper;
import su.vistar.db.mapper.SenderPhoneMapper;
import su.vistar.logging.LoggerManager;

public class SendMessage implements Action {

    public static final LoggerManager log = new LoggerManager();
    CommonFunctions commonFunctions = ServiceLocator.getCommonFunction();

    Logger logg = Logger.getLogger(su.vistar.action.SendMessage.class.getName());
//===================================================================================

    /**
     * Получает список сообщений по id отправителя.
     *
     * @param msg_sender_id id отправителя
     * @return сообщения,которые необходимо передать по указанному id
     */
    public List<MessageEntity> getMessages(int msg_sender_id) {
        List<MessageEntity> messagesList = new ArrayList<>();
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {

            MessageMapper mapp = session.getMapper(MessageMapper.class);
            messagesList = mapp.selectMessages(msg_sender_id);
            if (messagesList == null) {
                throw new RuntimeException("Не удалось загрузить список сообщений!Список пуст!");
            }

        }

        return messagesList;
    }
//=========================================================================================

    /**
     * Заполняет MessagesEntity полученными сообщениями.
     *
     * @param msg_sender_id id отправителя
     * @return объект MessagesEntity,с переменной messages,в которой все
     * сообщения,которые необходимо отправить по заданном id
     */
    public MessagesEntity getMessagesEntity(int msg_sender_id) {
        MessagesEntity messagesEntity = new MessagesEntity();
        List<MessageEntity> messagesList = new ArrayList<>();
        try {
            messagesList = getMessages(msg_sender_id);
            messagesEntity.setMessages(messagesList);
        } catch (RuntimeException re) {
            log.severe(re, re.getMessage());
        }

        return messagesEntity;
    }

//==============================================================================================
    /**
     * Получение id отпрвителя по Imei устройства,принадлежащего отправителю.
     *
     * @param sdpImei imei устройства
     * @return id отправителя
     */
    public Integer getId(String sdpImei, String sdpPassword) {

        Integer id = null;

        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            SenderPhoneMapper mapp = session.getMapper(SenderPhoneMapper.class);
            id = mapp.getIdByImei(sdpImei, sdpPassword);
            if (id == null) {
                throw new RuntimeException("Не удалось получить id по знаению Imei " + sdpImei);
            }
        }catch(BindingException e){
            throw new RuntimeException("Не удалось получить id по знаению Imei " + sdpImei);
        }

        return id;
    }

//=============================================================================================
    /**
     * Возвращает объект json для отправки ответа устройству,отправившему
     * запрос.
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public JsonObject execute(HttpServletRequest request, HttpServletResponse response) {

        HashMap<String, String> params = DatabaseServlet.getParams();
        boolean status = true;
        ObjectMapper mapper = new ObjectMapper();
        final JsonObject obj = new JsonObject();
        final Gson gson = new Gson();
        Integer id = null;
        String message = "";

        MessagesEntity messagesEntity = null;
        GetPasswordEntity getPasswordEntity = null;

        //Получение imei устройства в виде строки json из запроса
        try {
            getPasswordEntity = mapper.readValue(params.get("json"), GetPasswordEntity.class);
        } catch (IOException e) {
            status = false;
            message = "Не удалось подключиться!Неверный запрос!";
            log.severe(e, "Ошибка в получении строки JSON для объекта GetMessageEntity!");
        }
        //Получение imei устройства
        String sdp_imei = getPasswordEntity.getSdp_imei();
        String sdp_password = getPasswordEntity.getSdp_password();

        //Получение id отправителя по номеру imei
        try {
            id = getId(sdp_imei, sdp_password);
        } catch (RuntimeException e) {
            status = false;
            message = "Неверный пароль!Или устройство не значится в БД!";
            log.severe(e, e.getMessage());
        }
        //Получение сообщений,предназначенных конкретному отправителю
        try {
            messagesEntity = getMessagesEntity(id);
        } catch (RuntimeException re) {
            status = false;
            message = "Неверный пароль!Или устройство не значится в БД!";
            log.severe(re, re.getMessage());
        }
        //Изменение статуса сообщения с NOT_SEND на SEND_TO_PHONE при отправки его на устройство отправителя
        if (status == true) {
            List<MessageEntity> messagesList = messagesEntity.getMessages();
            for (Iterator<MessageEntity> iterator = messagesList.iterator(); iterator.hasNext();) {
                MessageEntity next = iterator.next();
                Integer messageId = next.getMsg_id();
                Date currentDate = new Date();
                commonFunctions.setSendToPhoneTime(currentDate, messageId);
                commonFunctions.setSendStatus(messageId, "SEND_TO_PHONE");

            }
        }
        //Заполнение json объекта
            try {
                obj.addProperty("Messages", gson.toJson(messagesEntity));
                logg.severe(obj.toString());
            } catch (RuntimeException e) {
                status = false;
                log.severe(e, "Ошибка в получении строки JSON из объекта MessagesEntity!");
            }


        if (status == false) {
            obj.addProperty("Status", gson.toJson(status));
            obj.addProperty("Error", message);
        }
        return obj;
    }

}
