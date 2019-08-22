package exceptions;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException() {
        super("Service unavailable.");
    }

    public ServiceUnavailableException(String message) {
        super("Service unavailable: "+message);
    }
}
