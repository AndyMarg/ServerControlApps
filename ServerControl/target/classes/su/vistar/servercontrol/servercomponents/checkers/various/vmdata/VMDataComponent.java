package su.vistar.servercontrol.servercomponents.checkers.various.vmdata;

import java.util.Date;
import su.vistar.marshrut.data.client.VMDataClient;
import su.vistar.marshrut.data.client.request.RoutesRequestExecutor;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent;
import su.vistar.servercontrol.servercomponents.ServerComponent;

public class VMDataComponent extends ServerComponent {

    //Конструктор для компонента с названием сервера 
    public VMDataComponent(String serverName) {
        this.serverName = serverName;
    }

//==========================================================================================================
    /**
     * Проверка активности сервера.
     *
     * @return состояние сервера на момент проверки
     */
    @Override
    public CurrentStateForComponent.ComponentState performCheck() {
        CurrentStateForComponent.ComponentState state = new CurrentStateForComponent.ComponentState(
                isActive, isCheckError, checkErrorDescription, lastCheckTime, lastSuccessCheckTime, serverStopTime, statusDescription);
        RoutesRequestExecutor.RouteData allRoutes = new RoutesRequestExecutor.RouteData();

        try {
            //Проверка полученного списока подключений на пустоту и вызов соответствующего исключения
            allRoutes = checkRoutesList();
        } catch (ServerErrorException e) {
            state.statusDescription = e.statusDescription;
            state.isActive = false;
            state.lastCheckTime = new Date();
            state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
            state.serverStopTime = new Date();
            currentState.componentState.put(this.serverName, state);
            return state;
        }

        //Ошибка сервера при получении пустого списка данных по маршрутам
        if (allRoutes.routes.isEmpty()) {
            state.statusDescription = "Не загружаются данные по маршрутам!";
            state.isActive = false;
            state.lastCheckTime = new Date();
            state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
            state.serverStopTime = new Date();
        } else {
            state.statusDescription = "Работает исправно.";
            state.isActive = true;
            state.lastCheckTime = new Date();
            state.lastSuccessCheckTime = new Date();
            state.serverStopTime = null;
        }
        
        
        currentState.componentState.put(this.serverName,state);
        return state;
    }
//=========================================================================================================

    /**
     * Проверка загрузки списка маршрутов компонента VMData.
     *
     * @return список маршрутов
     */
    public RoutesRequestExecutor.RouteData checkRoutesList() {
        VMDataClient vmDataClient = new VMDataClient(true);
        //Получение списка маршрутов
        RoutesRequestExecutor.RouteData allRoutes = vmDataClient.getAllRoutes(null);

        //Вызов исключения ServerErrorException при получении пустого списка маршрутов
        if (allRoutes == null) {
            throw new ServerErrorException("Не загружается список маршрутов!");
        }

        return allRoutes;
    }
}
