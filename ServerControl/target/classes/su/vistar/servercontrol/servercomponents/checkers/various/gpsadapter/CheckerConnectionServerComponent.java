package su.vistar.servercontrol.servercomponents.checkers.various.gpsadapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent;
import su.vistar.servercontrol.servercomponents.ServerComponent;

public abstract class CheckerConnectionServerComponent extends ServerComponent {

//=======================================================================================
    /**
     * Получение условия проверки активности сервера.
     *
     * @return условие проверки
     */
    protected abstract Map<String, ArrayList<String>> getConnectionList();

//======================================================================================
    /**
     * Проверка активности сервера.
     *
     * @return состояние сервера на момент проверки
     */
    @Override
    public CurrentStateForComponent.ComponentState performCheck() {
        CurrentStateForComponent.ComponentState state = new CurrentStateForComponent.ComponentState(
                isActive, isCheckError, checkErrorDescription, lastCheckTime, lastSuccessCheckTime, serverStopTime, statusDescription);

        Map<String, ArrayList<String>> map = new HashMap<>();
        //Получение ошибки сервера и выбрасывание исключеня ошибки проверки
        try {
            //Получение hashMap для списка подключений к каждому адаптеру
            map = getConnectionList();

            //Вызов ServerErrorException в случае пустоты списка подключений
        } catch (ServerErrorException e) {
            state.statusDescription = e.statusDescription;
            state.isActive = false;
            state.lastCheckTime = new Date();
            state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
            state.serverStopTime = new Date();
            currentState.componentState.put(this.serverName, state);
            return state;
        }catch (Exception er) {
            throw new CheckFailureException(er,"Ошибка проверки!Ошибка: " + er);
        }
        if (this.status == 200) {
            for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {

                ArrayList<String> channels = entry.getValue();
                String adapterName = entry.getKey();
                int arraySize = channels.size();
                //Проверка на количество подключенных устройств  для адаптера,при нормальной
                // работе сервера,у каждого адаптера должно быть хотя бы одно подключенное устройство
                if (arraySize < 1) {
                    state.statusDescription = "У адаптера " + adapterName + " нет ни одного подключенного устройства!";
                    state.isActive = false;
                    state.lastCheckTime = new Date();
                    state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
                    state.serverStopTime = new Date();
                }
                else{
                    state.statusDescription = "Работает исправно.";
                    state.isActive = true;
                    state.lastCheckTime = new Date();
                    state.lastSuccessCheckTime = new Date();
                    state.serverStopTime = null;
                }
            }
        } else {
            //Проверка кодов ошибки(404,500,503)
            try {
                checkServerErrors(this.status);
            } catch (ServerErrorException e) {
                state.statusDescription = e.statusDescription;
                state.isActive = false;
                state.lastCheckTime = new Date();
                state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
                state.serverStopTime = new Date();
                currentState.componentState.put(this.serverName, state);
                return state;
            }
        }

        currentState.componentState.put(this.serverName,state);
        return state;
    }

}
