package pavlina.EShop.exception_handling.exceptions;

public class DatabaseEmptyException extends RuntimeException{
    public DatabaseEmptyException() {
        super("No data stored in database");
    }
}
