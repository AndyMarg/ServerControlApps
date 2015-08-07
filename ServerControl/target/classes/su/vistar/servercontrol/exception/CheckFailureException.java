package su.vistar.servercontrol.exception;

public class CheckFailureException extends RuntimeException {

    public final String description;

    //===================================================================================     

    /**
     * Вызов исключения для получения статуса ошибки проверки.
     *
     * @param description описание ошибки проверки
     */
    public CheckFailureException(String description) {
        this.description = description;
    }

    /**
     * Вызов исключения для получения статуса ошибки неактивности сервера.
     *
     * @param description описание ошибки работы сервера.
     * @param cause ошибка,являющаяся причиной ошибки сервера
     */
    public CheckFailureException(Throwable cause, String description) {
        super(cause);
        this.description = description;
    }
}
