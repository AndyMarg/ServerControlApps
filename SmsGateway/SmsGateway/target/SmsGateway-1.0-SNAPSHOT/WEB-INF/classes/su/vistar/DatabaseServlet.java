package su.vistar;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import su.vistar.action.Action;
import su.vistar.action.AddMessage;
import su.vistar.action.ChangeMessageStatus;
import su.vistar.action.CheckDevicePassword;
import su.vistar.action.SendMessage;
import su.vistar.logging.LoggerManager;

public class DatabaseServlet extends HttpServlet {

    static HashMap<String, String> params = new HashMap<>();
    private static final LoggerManager log = new LoggerManager();
    private final Map<String, Action> dataBaseActions = new HashMap<>();
    
    
//=====================================================================================

    /**
     * Заполняем hashMap значениями в виде строчки с названием запроса в виде
     * ключа и класса типа Action в виде значения. reqAddMessage: добавление
     * сообщения в базу данных reqSendMessage: отправка сообщений,полученнных из
     * базы для конкретного imei,приложению,сделавшему запрос
     * reqChangeMessageStatus: смена статуса сообщения в БД на 'SEND' при
     * удачной отправке сообщения на приложении
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {

        super.init();
        dataBaseActions.put("reqAddMessage", new AddMessage());
        dataBaseActions.put("reqSendMessage", new SendMessage());
        dataBaseActions.put("reqChangeMessageStatus", new ChangeMessageStatus());
        dataBaseActions.put("reqCheckPassword", new CheckDevicePassword());
    }
    //==================================================================================================================================

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        final PrintWriter out = response.getWriter();
        //Получение параметров запроса
         params = getRequestData(request);
        //Получение команды для выбора нужного Action из hashMap
        String req = params.get("command");
        try {
            //если название запроса есть в hashMap,то выполняем соответстующий Action и заполняем объект JSON
            if (dataBaseActions.containsKey(req)) {
                final JsonObject obj = dataBaseActions.get(req).execute(request, response);
                out.println(obj.toString());
                //иначе сообщем об отсутствии обработчика для запроса
            } else {
                final JsonObject failRequest = new JsonObject();
                failRequest.addProperty("success", false);
                failRequest.addProperty("error", String.format("Не найден обработчик для запроса : %s ", req));
                out.println(failRequest);
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, e, "Ошибка в сервлете DatabaseServlet. Тип запроса {0}", req);

            final JsonObject obj = new JsonObject();
            obj.addProperty("success", false);
            obj.addProperty("error", e.getMessage());
            out.println(obj.toString());
        } finally {
            out.close();
        }
    }
//==================================================================================================================

    /**
     * Получает параметры запроса в виде HashMap,где первый параметр тип
     * запроса,а второй строка json
     *
     * @param request
     * @return hashMap в виде параметров запроса
     */
    public HashMap<String, String> getRequestData(HttpServletRequest request) {

        HashMap<String, String> result = new HashMap<>();

        try {
            //Получение строки запроса
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String data = in.readLine();
            //Делим строку на элементы массива,разделив ее по символу '&'
            String[] pairs = data.split("&");
            //Заполнение hashMap
            for (String s : pairs) {
                String key = s.substring(0, s.indexOf('='));
                String value = s.substring(s.indexOf('=') + 1);
                result.put(key, value);
            }
        } catch (IOException e) {
            log.severe(e, "Ошибка получения параметров!");
        }

        return result;
    }

//===================================================================================================================   
    /**
     * Возвращает параметры запроса.
     *
     * @return hashMap в виде параметров запроса
     */
    public static HashMap<String, String> getParams() {
        return params;
    }

//===================================================================================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

//====================================================================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

//=====================================================================================================================
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
