package pavlina.EShop.exception_handling.exceptions;

public class CartEmptyException extends RuntimeException{
    public CartEmptyException() {
        super("Cart is empty");
    }
}
