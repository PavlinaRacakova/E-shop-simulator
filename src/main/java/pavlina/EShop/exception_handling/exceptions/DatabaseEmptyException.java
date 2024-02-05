package pavlina.EShop.exception_handling.exceptions;

/**
 * Exception that is returned when database is empty
 */
public class DatabaseEmptyException extends RuntimeException{
    public DatabaseEmptyException() {
        super("No data stored in database");
    }
}
