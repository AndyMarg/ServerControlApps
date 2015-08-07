package su.vistar.servercontrol.exception;

public class ConnectionException extends RuntimeException {

    public final String description;
//===================================================================================     

    /**
     * Вызов исключения при ошибках соединения.
     *
     * @param description описание ошибки
     */
    public ConnectionException(String description) {
        this.description = description;
    }
}
