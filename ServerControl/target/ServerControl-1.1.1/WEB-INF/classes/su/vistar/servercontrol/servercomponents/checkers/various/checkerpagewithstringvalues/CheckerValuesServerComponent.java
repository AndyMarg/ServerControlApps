package su.vistar.servercontrol.servercomponents.checkers.various.checkerpagewithstringvalues;

import java.util.Date;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent;
import su.vistar.servercontrol.servercomponents.ServerComponent;

public abstract class CheckerValuesServerComponent extends ServerComponent {

    //предыдущее условие проверки для сравнения и получения состояния сервера
    private Integer lastValue = null;
//====================================================================================

    /**
     * Получение условия проверки активности сервера.
     *
     * @return условие проверки
     */
    protected abstract int getCurrentValue();

//====================================================================================
    /**
     * Проверка активности сервера.
     *
     * @return состояние сервера на момент проверки
     */
    @Override
    public CurrentStateForComponent.ComponentState performCheck() {

        CurrentStateForComponent.ComponentState state = new CurrentStateForComponent.ComponentState(
                isActive, isCheckError, checkErrorDescription, lastCheckTime, lastSuccessCheckTime, serverStopTime, statusDescription);
        //Получение условия проверки
        int value = 0;
        //Получение ошибки сервера и выбрасывание исключеня ошибки проверки
        try {
            value = getCurrentValue();
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
        //Проверка на статус 200(ОК)
        if (this.status == 200) {
            if (lastValue != null) {
                state = fillComponentstate(lastValue, value);
                lastValue = value;
            } else {
                //Выполняется при первом запуске приложения на сервере
                lastValue = value;
                state.lastCheckTime = new Date();
                state.lastSuccessCheckTime = new Date();
                state.statusDescription = "Работает исправно.";
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

        currentState.componentState.put(this.serverName, state);
        return state;
    }
}
