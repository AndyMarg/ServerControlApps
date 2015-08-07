package su.vistar.servercontrol.servercomponents;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrentStateForComponent {

    public Map<String, ComponentState> componentState = new ConcurrentHashMap<>();

    public static class ComponentState {

        public boolean isActive;
        public String statusDescription;
        public Date lastCheckTime;
        public Date lastSuccessCheckTime;
        public Date serverStopTime;

        public boolean isCheckError;
        public String checkErrorDescription;

//=====================================================================================================================
        /**
         * Получение текущего состояния сервер компнента.
         *
         * @param isActive Статус активности сервера
         * @param isCheckError Есть ли ошибка проверки
         * @param checkErrorDescription Описание ошибки проверки
         * @param lastCheckTime Время последней проверки сервера
         * @param lastSuccessCheckTime Время последней успешной проверки
         * @param serverStopTime Время остановки сервера при его статусе "Не
         * работает"
         * @param statusDescription описание ошибки сервера
         */
        public ComponentState(boolean isActive, boolean isCheckError, String checkErrorDescription, Date lastCheckTime, Date lastSuccessCheckTime, Date serverStopTime, String statusDescription) {
            this.isActive = isActive;
            this.lastCheckTime = lastCheckTime;
            this.lastSuccessCheckTime = lastSuccessCheckTime;
            this.serverStopTime = serverStopTime;
            this.statusDescription = statusDescription;
            this.isCheckError = isCheckError;
            this.checkErrorDescription = checkErrorDescription;
        }
    }

//=======================================================================================================================
    /**
     * Добавление текущего состояния сервер компнента в Map.
     *
     * @param checker сервер компонент для которого вызывается функция
     */
    public void setComponentStateMap(ServerComponent checker) {

        componentState.put(checker.serverName, checker.performCheck());

    }
//=======================================================================================================================

    /**
     * Получение текущего состояния сервер компнента в виде объекта Map.
     *
     * @return текущее состояние всех серверов
     */
    public Map<String, ComponentState> getCurrentState() {
        return componentState;
    }

}
