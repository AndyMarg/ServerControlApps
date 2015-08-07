package su.vistar.servercontrol.servercomponents.checkers.various.сhekerpagewithlongvalues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent;
import su.vistar.servercontrol.servercomponents.ServerComponent;

public abstract class ChekerTimeServerComponent extends ServerComponent {

    LoggerManager log = new LoggerManager();
    Map<String, ArrayList<Integer>> checkFirstCheckRoute = new HashMap<>();
    Map<String, ArrayList<Integer>> checkSecondCheckRoute = new HashMap<>();

//====================================================================================
    /**
     * Получение условия проверки активности сервера.
     *
     * @param routeName имя маршрута для которого надо полчить hashMap
     * @return условие проверки
     * @throws java.lang.Exception
     */
    protected abstract Map<String, ArrayList<Integer>> getTimeList(String routeName) throws Exception;

//=====================================================================================
    /**
     * Проверка активности сервера.
     *
     * @return состояние сервера на момент проверки
     */
    @Override
    public CurrentStateForComponent.ComponentState performCheck() {
        CurrentStateForComponent.ComponentState state = new CurrentStateForComponent.ComponentState(
                isActive, isCheckError, checkErrorDescription, lastCheckTime, lastSuccessCheckTime, serverStopTime, statusDescription);
        Map<String, ArrayList<Integer>> firstRouteMap = new HashMap<>();
        Map<String, ArrayList<Integer>> secondRouteMap = new HashMap<>();
        //Получение ошибки сервера и выбрасывание исключеня ошибки проверки
        try {
            //Получение hashMap для первого проверочного маршрута
            firstRouteMap = getTimeList("firstRoute");
            //Аналогично для второго
            secondRouteMap = getTimeList("secondRoute");
            //Вызов ServerErrorException в случае пустоты списка для выбранного маршрута
        } catch (ServerErrorException e) {
            state.statusDescription = e.statusDescription;
            state.isActive = false;
            state.lastCheckTime = new Date();
            state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
            state.serverStopTime = new Date();
            currentState.componentState.put(this.serverName, state);
            return state;
        } catch (Exception er) {
            throw new CheckFailureException(er,"Ошибка проверки!Ошибка: " + er);
        }
        
        //Проверка на статус 200(ОК)
        if (this.status == 200) {
            //Получение состояния активности  сервера для первого маршрута
            CurrentStateForComponent.ComponentState firstState = checkStateForRoute(checkFirstCheckRoute, firstRouteMap);
            //Аналогично для второго
            CurrentStateForComponent.ComponentState secondState = checkStateForRoute(checkSecondCheckRoute, secondRouteMap);
            //Если при выборе обоих маршрутов сервер работает исправно,заключается,что сервер активен
            if ((firstState.isActive == true) && (secondState.isActive == true)) {

                state.statusDescription = "Работает исправно.";
                state.isActive = true;
                state.lastCheckTime = new Date();
                this.tempLastSuccessCheckTime = state.lastSuccessCheckTime = new Date();
                state.serverStopTime = null;
            } else {

                state.statusDescription = "Медленно обрабатывает данные.";
                state.isActive = false;
                state.lastCheckTime = new Date();
                state.lastSuccessCheckTime = this.tempLastSuccessCheckTime;
                state.serverStopTime = new Date();
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

//==========================================================================================
    /**
     * Получение состояния активности сервера при выборе конкретного маршрута.
     *
     * @param checkMap предыдущий hashMap,сохранненый в буфере,для анализа и
     * выполнения проверки
     * @param mainMap hashMap текущий,полученнный при нахождений условий
     * проверки
     * @return состояние сервера на момент проверки при выборе конкретного
     * маршрута
     */
    private CurrentStateForComponent.ComponentState checkStateForRoute(Map<String, ArrayList<Integer>> checkMap, Map<String, ArrayList<Integer>> mainMap) {
        //Счетчик количества проверяемых моментов времени
        int count = 0;
        //Счетчик совпадений элементов массива моментов времени checkMap и элементов массива mainMap
        int checkCount = 0;
        CurrentStateForComponent.ComponentState state = new CurrentStateForComponent.ComponentState(
                isActive, isCheckError, checkErrorDescription, lastCheckTime, lastSuccessCheckTime, serverStopTime, statusDescription);
        if (checkMap.size() != 0) {
            //Цикл получения ключей и значений mainMap
            for (Entry<String, ArrayList<Integer>> entry : mainMap.entrySet()) {
                //Получение массива моментов времени
                ArrayList<Integer> array = entry.getValue();
                Collections.sort(array);
                count += array.size();
                //Получение имени маршрута(ключ)
                String route = entry.getKey();
                //Получение массива моментов времени в checkMap по заданному имени маршрута(ключу)
                ArrayList<Integer> checkArray = checkMap.get(route);
                //Попарное сравнение элементов массивов,если элементы совпадают,то увеличиваем счетчик
                for (int i = 0; i < array.size(); i++) {
                    if (checkArray.get(i) == array.get(i)) {
                        checkCount++;
                    }

                }
            }
            //если все элементы всех массивов для маршрутов совпали,
            //значит работа сервера для выбранного маршрута приостановлена,иначе сервер работает исправно
            state = fillComponentstate(checkCount, count);
            checkMap = mainMap;
            //Выполняется при первом запуске приложения на сервере
        } else {
            checkMap = mainMap;
            state.statusDescription = "Работает исправно.";
            state.isActive = true;
            state.lastCheckTime = new Date();
            state.lastSuccessCheckTime = new Date();
            state.serverStopTime = null;
        }

        return state;
    }

}
