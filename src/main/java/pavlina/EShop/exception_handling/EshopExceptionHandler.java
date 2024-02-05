package pavlina.EShop.exception_handling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.OrderNotFoundException;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;

/**
 * Exception Handler for all controllers
 */

@RestControllerAdvice
public class EshopExceptionHandler {

    @ExceptionHandler(DatabaseEmptyException.class)
    public ResponseEntity<?> handleNoDataInDatabase(DatabaseEmptyException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleMissingProducts(ProductNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleMissingOrders(OrderNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
