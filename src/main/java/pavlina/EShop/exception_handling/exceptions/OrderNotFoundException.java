package pavlina.EShop.exception_handling.exceptions;

/**
 * Exception that is returned when requested order is not found
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("No such order(s) found");
    }
}
