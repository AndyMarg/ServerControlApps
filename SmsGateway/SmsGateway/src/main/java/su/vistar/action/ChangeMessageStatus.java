package su.vistar.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import su.vistar.DatabaseServlet;
import su.vistar.util.ServiceLocator;
import su.vistar.db.CommonFunctions;
import su.vistar.db.entity.MessageStatusEntity;
import su.vistar.db.entity.MessagesStatusEntity;
import su.vistar.logging.LoggerManager;

public class ChangeMessageStatus implements Action {

    public static final LoggerManager log = new LoggerManager();
    CommonFunctions commonFunctions = ServiceLocator.getCommonFunction();

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

        MessageStatusEntity messageStatusEntity = null;

        HashMap<String, String> params = DatabaseServlet.getParams();
        ObjectMapper mapper = new ObjectMapper();
        final JsonObject obj = new JsonObject();
        final Gson gson = new Gson();
        boolean status;
        //Получение запроса об смене статуса в виде строки json
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            messageStatusEntity = mapper.readValue(params.get("json"), MessageStatusEntity.class);
            //Когда сообщение на устройстве отправлено адресатам меняем его статус в БД с SEND_TO_PHONE на SEND
                Integer messageId = messageStatusEntity.getMessageId();
                Date sendDate = format.parse(messageStatusEntity.getMessageSendDate());
                String sendStatus = messageStatusEntity.getStatus();
                commonFunctions.setSendTime(sendDate, messageId);
                commonFunctions.setSendStatus(messageId, sendStatus);

            status = true;

        } catch (IOException e) {
            status = false;
            log.severe(e, "Ошибка в получении строки JSON для объекта GetMessageEntity!");
        } catch (ParseException e){
            status = false;
            log.severe(e, "Ошибка приведения к типу даты!");
        }

        obj.addProperty("status", gson.toJson(status));
        return obj;
    }

}
