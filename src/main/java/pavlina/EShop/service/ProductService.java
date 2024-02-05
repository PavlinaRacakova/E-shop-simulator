package pavlina.EShop.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pavlina.EShop.entities.product.Product;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;
import pavlina.EShop.repository.ProductRepository;

import java.util.List;

/**
 * Product service class
 */
@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAllProducts() {
        List<Product> products = repository.findAll();
        if (!products.isEmpty()) {
            return products;
        } else {
            throw new DatabaseEmptyException();
        }
    }

    public List<Product> findAllProductsCheaperThan(int price) {
        List<Product> products = repository.findByPriceLessThanEqual(price);
        if (!products.isEmpty()) {
            return products;
        } else {
            throw new ProductNotFoundException();
        }
    }

    public List<Product> findAllProductsMoreExpensiveThan(int price) {
        List<Product> products =  repository.findByPriceGreaterThanEqual(price);
        if (!products.isEmpty()) {
            return products;
        } else {
            throw new ProductNotFoundException();
        }
    }

    public Product findProductById(int id) {
        return repository.findById(id).orElseThrow(
                ProductNotFoundException::new);
    }

    public List<Product> findAllProductsThatArentSold() {
        List<Product> products =   repository.findAllByOrderIsNull();
        if (!products.isEmpty()) {
            return products;
        } else {
            throw new ProductNotFoundException();
        }
    }

    public ResponseEntity<?> saveNewProduct(Product product) {
        repository.save(product);
        return ResponseEntity.ok().body("New product successfully added");
    }
}
