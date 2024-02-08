package pavlina.EShop.exception_handling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pavlina.EShop.exception_handling.exceptions.*;

/**
 * Exception Handler for all controllers
 */

@RestControllerAdvice
public class EshopExceptionHandler {

    @ExceptionHandler(DatabaseEmptyException.class)
    public ResponseEntity<?> handleNoDataInDatabase(DatabaseEmptyException exception) {
        return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleMissingProducts(ProductNotFoundException exception) {
        return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    public ResponseEntity<?> handleMissingProducts(ProductNotAvailableException exception) {
        return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleMissingOrders(OrderNotFoundException exception) {
        return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<?> handleEmptyCart(CartEmptyException exception) {
        return ResponseEntity.status(409).body(new ErrorMessage(exception.getMessage()));
    }
}
