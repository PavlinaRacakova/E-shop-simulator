package pavlina.EShop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pavlina.EShop.service.CartService;

/**
 * Controller for cart related queries
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable int productId, HttpSession session) {
        return service.addToTheCart(productId, session);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable int productId, HttpSession session) {
        return service.removeFromTheCart(productId, session);
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAllItemsInCart(HttpSession session) {
        return ResponseEntity.ok().body(service.getAllItemsAndTheirPrice(session));
    }
}
