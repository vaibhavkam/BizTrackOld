package exceptions;

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException() {
        super("Operation not supported.");
    }

    public InvalidOperationException(String message) {
        super("Operation not supported: "+message);
    }
}
