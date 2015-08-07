package su.vistar.servercontrol.servercomponents.checkers.various.chekerpagewithdatavalues;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent;
import su.vistar.servercontrol.servercomponents.ServerComponent;

public abstract class ChekerDatesServerComponent extends ServerComponent {

//====================================================================================
    /**
     * Получение условия проверки активности сервера.
     *
     * @return условие проверки
     */
    protected abstract List<Date> getDatesList();

//====================================================================================
    /**
     * Проверка активности сервера.
     *
     * @return состояние сервера на момент проверки
     */
    @Override
    public CurrentStateForComponent.ComponentState performCheck() {

        //Получение текущей даты
        Date currentDate = new Date();
        //5 минут назад от текущей даты,для корректной проверки активности сервера
        Date currentDateMinusFiveMinutes = new Date(currentDate.getTime() - 5 * 60_000);
        //Date currentDateMinusFiveMinutes = new Date(currentDate.getTime() + 10 * 60_000);
        CurrentStateForComponent.ComponentState state = new CurrentStateForComponent.ComponentState(
                isActive, isCheckError, checkErrorDescription, lastCheckTime, lastSuccessCheckTime, serverStopTime, statusDescription);
        //Получение списка условий для проверки
        List<Date> list = new ArrayList<>();
        //Получение ошибки сервера и выбрасывание исключеня ошибки проверки
        try {
            list = getDatesList();
        } catch (ServerErrorException e) {
            state.statusDescription = e.statusDescription;
            state.isActive = false;
            state.lastCheckTime = new Date();
            state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
            state.serverStopTime = new Date();
            currentState.componentState.put(this.serverName, state);
            return state;
        } catch (Exception er) {
            throw new CheckFailureException(er, "Ошибка проверки!Ошибка: " + er);
        }
        //Счетчик совпадений currentDateMinusFiveMinutes даты с условиями проверки в виде дат обновленния данных на сервере
        int counter = 0;
        //Проверка на статус 200(ОК)
        if (this.status == 200) {
            for (Iterator<Date> iterator = list.iterator(); iterator.hasNext();) {
                Date date = iterator.next();
                //Если дата обновления данных на сервере меньше  currentDateMinusFiveMinutes даты,увеличиваем счетчик 
                if (currentDateMinusFiveMinutes.compareTo(date) == 1) {
                    counter++;
                }
            }
            //если все даты обновления данных на сервере оказались меньше currentDateMinusFiveMinutes даты,
            //значит работа сервера приостановлена,иначе сервер работает исправно
            state = fillComponentstate(counter, list.size());
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
