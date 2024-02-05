package pavlina.EShop.exception_handling.exceptions;

/**
 * Exception that is returned when requested product is not found
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("No such product(s) found");
    }
}
