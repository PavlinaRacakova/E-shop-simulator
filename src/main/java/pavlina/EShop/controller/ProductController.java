package pavlina.EShop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.exception_handling.exceptions.ValidationException;
import pavlina.EShop.service.ProductService;

import java.net.URI;

/**
 * Controller handling product related requests
 */
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

    @PostMapping("/add")
    public ResponseEntity<?> addNewProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ValidationException(bindingResult));
        }
        service.saveNewProduct(product);
        return ResponseEntity.created(URI.create(
                        String.format("%s%s%s", ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(), "/products/", product.getId())))
                .build();
    }
}
