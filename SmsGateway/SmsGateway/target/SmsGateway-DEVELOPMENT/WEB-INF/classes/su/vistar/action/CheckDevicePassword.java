package su.vistar.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import su.vistar.DatabaseServlet;
import static su.vistar.action.SendMessage.log;
import su.vistar.db.DatabaseConnection;
import su.vistar.db.entity.GetPasswordEntity;
import su.vistar.db.entity.SenderPhoneEntity;
import su.vistar.db.mapper.SenderPhoneMapper;

public class CheckDevicePassword implements Action {

    //==============================================================================================
    /**
     * Получение id отпрвителя по Imei устройства,принадлежащего отправителю.
     *
     * @param sdpImei imei устройства
     * @return id отправителя
     */
    public SenderPhoneEntity getInformationByLoginAndPass(String sdpImei, String sdpPassword) {

        SenderPhoneEntity phoneEntity = null;

        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            SenderPhoneMapper mapp = session.getMapper(SenderPhoneMapper.class);
            phoneEntity = mapp.getInfoByPasswordAndImei(sdpImei, sdpPassword);
            if (phoneEntity == null) {
                throw new RuntimeException("Не правильный логин в виде imei: " + sdpImei + " или неверный пароль: " + sdpPassword + "!");
            }
        }

        return phoneEntity;
    }

//=============================================================================================
    @Override
    public JsonObject execute(HttpServletRequest request, HttpServletResponse response) {

        HashMap<String, String> params = DatabaseServlet.getParams();
        boolean status = true;
        String message = "Успешная авторизация!";
        ObjectMapper mapper = new ObjectMapper();
        final JsonObject obj = new JsonObject();
        final Gson gson = new Gson();

        SenderPhoneEntity phoneEntity = null;
        GetPasswordEntity getPasswordEntity = null;

        try {
            getPasswordEntity = mapper.readValue(params.get("json"), GetPasswordEntity.class);
        } catch (IOException e) {
            status = false;
            message = "Не удалось подключиться!Неверный запрос!";
            log.severe(e, "Ошибка в получении строки JSON для объекта GetPasswordEntity!");
        }

        String sdp_imei = getPasswordEntity.getSdp_imei();
        String sdp_password = getPasswordEntity.getSdp_password();

        try {
            phoneEntity = getInformationByLoginAndPass(sdp_imei, sdp_password);
        } catch (RuntimeException e) {
            status = false;
            message = "Неверный пароль!Или устройство не значится в БД!";
            log.severe(e, e.getMessage());
        }

        try {
            obj.addProperty("Message", message);
            obj.addProperty("Status", status);
        } catch (RuntimeException e) {
            log.severe(e, "Ошибка в получении строки JSON!");
        }
        
        return obj;

    }

}
