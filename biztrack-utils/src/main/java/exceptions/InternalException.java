package exceptions;

public class InternalException extends RuntimeException {

    public InternalException() {
        super("Internal service error.");
    }

    public InternalException(String message) {
        super("Internal service error: "+message);
    }
}
