package su.vistar.servercontrol.exception;

public class TimeoutException extends RuntimeException {

    public final String description;
//===================================================================================     

    /**
     * Вызов исключения при таймауте запроса к серверу.
     *
     * @param description описание ошибки
     */
    public TimeoutException(String description) {
        this.description = description;
    }
}
