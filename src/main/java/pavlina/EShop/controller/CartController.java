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

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable int productId, HttpSession session) {
        service.addToTheCart(productId, session);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable int productId, HttpSession session) {
        service.removeFromTheCart(productId, session);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAllItemsInCart(HttpSession session) {
        return ResponseEntity.ok().body(service.getAllItemsAndTheirPriceDTO(session));
    }
}
