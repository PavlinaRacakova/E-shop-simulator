package pavlina.EShop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pavlina.EShop.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok().body(service.findAllProducts());
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAllAvailableProducts() {
        return ResponseEntity.ok().body(service.findAllProductsThatArentSold());
    }

    @GetMapping("/cheaper-than-{price}")
    public ResponseEntity<?> getAllProductsCheaperThan(@PathVariable int price) {
        return ResponseEntity.ok().body(service.findAllProductsCheaperThan(price));
    }

    @GetMapping("/more-expensive-than-{price}")
    public ResponseEntity<?> getAllProductsMoreExpensiveThan(@PathVariable int price) {
        return ResponseEntity.ok().body(service.findAllProductsMoreExpensiveThan(price));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        return ResponseEntity.ok().body(service.findProductById(id));
    }
}
