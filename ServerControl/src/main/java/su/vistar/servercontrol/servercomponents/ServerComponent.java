package su.vistar.servercontrol.servercomponents;

import java.util.Date;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent.ComponentState;
import su.vistar.servercontrol.exception.ServerErrorException;
import su.vistar.servercontrol.utils.ServiceLocator;

public abstract class ServerComponent {


    /*
     * @param serverName Имя проверяемого сервера
     * @param htmlPage HTML код страницы для дальшейшего парсинга
     * @param isActive Статус активности сервера
     * @param lastCheckTime Время последней проверки сервера
     * @param lastSuccessCheckTime Время последней успешной проверки
     * @param serverStopTime Время остановки сервера при его статусе "Не работает"
     * @param status Статус http запроса через HttpConnection
     * @param statusDescription Описание статуса активности сервера
     * @param isMessageSend проверка отправки сообщения об ошибках сервера
     * @param isCheckError Есть ли ошибка проверки
     * @param checkErrorDescription Описание ошибки проверки
     */
    public boolean isCheckError = false;
    public String checkErrorDescription = null;
    
    
    protected String htmlPage = null;
    
    protected String serverName = null;
    protected boolean isActive = true;
    protected Date lastCheckTime = null;
    protected Date lastSuccessCheckTime = null;
    protected Date serverStopTime = null;
    protected String statusDescription = null;
    
    protected Date tempLastSuccessCheckTime = null;
    protected int status;
    
    public boolean isMessageSend = false;
    
    public CurrentStateForComponent currentState = ServiceLocator.getCurrentStateObject();

//====================================================================================
    
    /**
     * Проверка активности сервера.
     *
     * @return состояние сервера на момент проверки
     */
    abstract public ComponentState performCheck();

    public void initialize() {
    }
    
    public void shutdown() {
    }
    
//=====================================================================================
    /**
     * Заполнение полей ComponentState,при условии сравнения переданных
     * параметров проверки.
     *
     * @param comparedValue1 параметр сравнения (уникальный для типа компонента)
     * для дальнейшего получения статуса активности
     * @param comparedValue2 аналогично параметр для сравнения
     * @return состояние сервер компонента
     */
    public ComponentState fillComponentstate(int comparedValue1, int comparedValue2) {

        //Если параметры совпадают,значит состояние условий проверки осталось неизменным,из чего следует 
        //что  сервер не работает,время последней проверки текущее,время последней успешной проверки находится в tempLastSuccessCheckTime,
        //время остановки сервера также текущее
        if (comparedValue1 == comparedValue2) {
            statusDescription = "Медленно обрабатывает данные.";
            isActive = false;
            lastCheckTime = new Date();
            lastSuccessCheckTime = this.tempLastSuccessCheckTime;
            serverStopTime = new Date();
            //В противном случаесервер работает,время последней проверки текущее,время последней успешной проверки также текущее,
            //время остановки null
        } else {
            statusDescription = "Работает исправно.";
            isActive = true;
            lastCheckTime = new Date();
            this.tempLastSuccessCheckTime = lastSuccessCheckTime = new Date();
            serverStopTime = null;
        }

        CurrentStateForComponent.ComponentState state = new CurrentStateForComponent.ComponentState(isActive, isCheckError, checkErrorDescription, lastCheckTime, lastSuccessCheckTime, serverStopTime, statusDescription);

        return state;
    }
//========================================================================================

    /**
     * Проверка кодов ошибки и вызов исключения ServerErrorException с
     * соответствующим сообщением.
     *
     * @param status код состояния HTTP
     */
    public void checkServerErrors(int status) {
        if (status == 404) {
            throw new ServerErrorException("Код ошибки: " + status + " ,страница не найдена!");
        } else if (status == 500) {
            throw new ServerErrorException("Код ошибки: " + status + " ,внутренняя ошибка сервера!");
        } else if (status == 503) {
            throw new ServerErrorException("Код ошибки: " + status + " ,cлужба недоступна !");
        }else if (status == 0) {
            throw new ServerErrorException("Сервер " + this.serverName + " не найден!");
        }
    }      

}
