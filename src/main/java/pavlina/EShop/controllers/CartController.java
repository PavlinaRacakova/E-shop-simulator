package pavlina.EShop.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pavlina.EShop.entities.product.Product;
import pavlina.EShop.services.CartService;

import java.util.List;

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

    @GetMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable int productId, HttpSession session) {
        return service.removeFromTheCart(productId, session);
    }

    @GetMapping("/items")
    public List<Product> getAllItemsInCart(HttpSession session) {
        return service.getAllItemsInCart(session);
    }
}
