package su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut;

import java.util.Calendar;
import java.util.Date;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.CheckerIsNotReadyException;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent;
import su.vistar.servercontrol.servercomponents.ServerComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.ShadowClient;
import su.vistar.servercontrol.utils.ServiceLocator;

public class VIStarMarshrutComponent extends ServerComponent {

    Date currentDateMinusFortySeconds;

    //Конструктор для компонента с названием сервера 
    public VIStarMarshrutComponent(String serverName) {
        this.serverName = serverName;
    }
    ShadowClientAdapterHeir shadowClientAdapterHeir = ServiceLocator.getShadowClientAdapterHeir();

    @Override
    public CurrentStateForComponent.ComponentState performCheck() {

        //Получение текущей даты
        Date currentDate = new Date();
        //40 секунд назад от текущей даты,для корректной проверки активности сервера
        currentDateMinusFortySeconds = new Date(currentDate.getTime() - 40 * 1000);
        //5 минут назад от текущей даты,для корректной проверки активности сервера
        Date currentDateMinusFiveMinutes = new Date(currentDate.getTime() - 5 * 60_000);
        //Получение часа текущего дня
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);

        CurrentStateForComponent.ComponentState state = new CurrentStateForComponent.ComponentState(
                isActive, isCheckError, checkErrorDescription, lastCheckTime, lastSuccessCheckTime, serverStopTime, statusDescription);
                
        Date date = new Date(shadowClientAdapterHeir.disconnectedTime.getTime() - 10 * 1000);
        if (shadowClientAdapterHeir.connectedTime.compareTo(date) == 1) {
            throw new CheckerIsNotReadyException("Подключение к серверу не установлено");
        }
        
        try {
            checkConnection();
        } catch (ServerErrorException e) {
            state.statusDescription = e.statusDescription;
            state.isActive = false;
            state.lastCheckTime = new Date();
            state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
            state.serverStopTime = new Date();
            currentState.componentState.put(this.serverName, state);
            return state;
        }

            // проверяем, что пришли все необходимые данные для проверки
            if (shadowClientAdapterHeir.changeSectionTime == null) {
                throw new CheckerIsNotReadyException("Данные не пришли: changeSectionTime");
            }

            if ((time >= 9) && (time <= 22)) {
                if (currentDateMinusFiveMinutes.compareTo(shadowClientAdapterHeir.changeSectionTime) == 1) {
                    state.statusDescription = "Смены секции по маршруту не было более 5 минут";
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

            }

            currentState.componentState.put(this.serverName, state);
            return state;
        }

    

    private void checkConnection() {
        if ((shadowClientAdapterHeir.isConnected == false) && (currentDateMinusFortySeconds.compareTo(shadowClientAdapterHeir.disconnectedTime) == 1)) {
            throw new ServerErrorException("Соединение с сервером было прекращено более 40 секунд назад!");
        }
    }

    @Override
    public void initialize() {

        ShadowClient shadowClient = ServiceLocator.getShadowClient();
        shadowClient.start();
    }

    @Override
    public void shutdown() {

        ShadowClient shadowClient = ServiceLocator.getShadowClient();
        shadowClient.stop();
    }

}
