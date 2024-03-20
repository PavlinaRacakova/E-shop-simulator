package pavlina.EShop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pavlina.EShop.domain.product.ProductDTO;
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
    public ResponseEntity<?> addToCart(@PathVariable int productId) {
        ProductDTO addedProduct = service.addToTheCart(productId);
        return ResponseEntity.ok().body(addedProduct);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable int productId) {
        ProductDTO removedProduct = service.removeFromTheCart(productId);
        return ResponseEntity.ok().body(removedProduct);
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAllItemsInCart() {
        return ResponseEntity.ok().body(service.getAllItemsAndTheirPriceDTO());
    }
}
