package pavlina.EShop.exception_handling.exceptions;

public class ProductNotAvailableException extends RuntimeException{
    public ProductNotAvailableException() {
        super("This product is currently unavailable");
    }
}
