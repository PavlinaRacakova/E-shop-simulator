package pavlina.EShop.exception_handling.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("No such order(s) found");
    }
}
