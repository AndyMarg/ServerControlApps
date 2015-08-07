package su.vistar.servercontrol.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.entity.ServerEntity;
import su.vistar.servercontrol.entity.ServerListEntity;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent.ComponentState;
import su.vistar.servercontrol.utils.ServiceLocator;

public class ServerServlet extends HttpServlet {

    private static final LoggerManager log = new LoggerManager();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Отправка строки json из объекта ServerListEntity
        ObjectMapper mapper = new ObjectMapper();
        ServerListEntity servers = getServersStates();
        //ПРоверка ошибок ServerServlet
        try (OutputStream out = response.getOutputStream()) {
            mapper.writeValue(out, servers);
        } catch (Exception e) {
            log.log(Level.SEVERE, e, "Ошибка в ServerServlet.");
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private ServerListEntity getServersStates() {

        CurrentStateForComponent currentState = ServiceLocator.getCurrentStateObject();
        //Получение текущего состояния серверов
        Map<String, ComponentState> map = currentState.getCurrentState();
        ServerListEntity serverListEntity = new ServerListEntity();
        //Заполнение объекта ServerListEntity для оправки клиенту через строку json
        for (Entry<String, ComponentState> entry : map.entrySet()) {
            String serverName = entry.getKey();
            ComponentState state = entry.getValue();
            ServerEntity serverEntity = new ServerEntity();
            //Заполнение объекта serverEntity
            serverEntity.serverName = serverName;
            serverEntity.isActive = state.isActive;
            serverEntity.lastCheckTime = state.lastCheckTime;
            serverEntity.lastSuccessCheckTime = state.lastSuccessCheckTime;
            serverEntity.serverStopTime = state.serverStopTime;
            serverEntity.statusDescription = state.statusDescription;
            serverEntity.isCheckError = state.isCheckError;
            serverEntity.checkErrorDescription = state.checkErrorDescription;

            //Добавление сервера к массиву servers объекта ServerListEntity 
            serverListEntity.addServer(serverEntity);

        }
        return serverListEntity;
    }

}
