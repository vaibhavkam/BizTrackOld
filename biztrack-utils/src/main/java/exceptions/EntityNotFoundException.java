package exceptions;

/**
 * Custom exception
 */
//@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, long id) {
        super(entityName+" with id "+id+" not found");
    }

}
