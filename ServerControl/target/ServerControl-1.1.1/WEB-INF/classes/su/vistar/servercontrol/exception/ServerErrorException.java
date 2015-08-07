package su.vistar.servercontrol.exception;

public class ServerErrorException extends RuntimeException {

    public final String statusDescription;

//===================================================================================     
    /**
     * Вызов исключения для получения статуса ошибки неактивности сервера.
     *
     * @param statusDescription описание ошибки работы сервера.
     */
    public ServerErrorException(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    /**
     * Вызов исключения для получения статуса ошибки неактивности сервера.
     *
     * @param statusDescription описание ошибки работы сервера.
     * @param cause ошибка,являющаяся причиной ошибки сервера
     */
    public ServerErrorException(Throwable cause, String statusDescription) {
        super(cause);
        this.statusDescription = statusDescription;
    }
}
