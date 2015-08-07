package su.vistar.servercontrol.exception;

public class CheckerIsNotReadyException extends CheckFailureException {

//===================================================================================     
    /**
     * Вызов исключения для получения статуса при неподготовленных данных
     * проверки.
     *
     * @param description описание ошибки проверки
     */
    public CheckerIsNotReadyException(String description) {
        super(description);
    }

}
