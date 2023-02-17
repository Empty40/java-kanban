package Exceprions;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message, final Throwable cause) {
        super(message, cause);
    }
}
