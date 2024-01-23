package pavlina.EShop.exception_handling.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException() {
        super("No such product(s) found");
    }
}
