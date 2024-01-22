package pavlina.EShop.service;

import org.springframework.stereotype.Service;
import pavlina.EShop.entities.product.Product;
import pavlina.EShop.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAllProducts() {
        return repository.findAll();
    }

    public List<Product> findAllProductsCheaperThan(int price) {
        return repository.findByPriceLessThanEqual(price);
    }

    public List<Product> findAllProductsMoreExpensiveThan(int price) {
        return repository.findByPriceGreaterThanEqual(price);
    }
}
