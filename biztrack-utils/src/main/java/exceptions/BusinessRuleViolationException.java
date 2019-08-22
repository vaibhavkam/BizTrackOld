package exceptions;

public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException() {
        super("Operation declined.");
    }

    public BusinessRuleViolationException(String message) {
        super("Operation declined: "+message);
    }
}
