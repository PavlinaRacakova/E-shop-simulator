package pavlina.EShop.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import pavlina.EShop.domain.order.Order;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;
import pavlina.EShop.repository.ProductRepository;

import java.util.List;

/**
 * Service class for product controller
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
        List<Product> products = repository.findByPriceGreaterThanEqual(price);
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
        List<Product> products = repository.findAllByOrderIsNullAndSessionIdIsNull();
        if (!products.isEmpty()) {
            return products;
        } else {
            throw new ProductNotFoundException();
        }
    }

    public void saveNewProduct(Product product) {
        repository.save(product);
    }

    public void markProductsAsSold(Order order, List<Product> productsInCart) {
        for (Product product : productsInCart) {
            product.setOrder(order);
            repository.save(product);
        }
    }

    public void markProductAsReserved(Product product, HttpSession session) {
        product.setSessionId(session.getId());
        repository.save(product);
    }

    public void releaseProductsForExpiredSession(String sessionId) {
        List<Product> productsToMarkAsAvailable = repository.findBySessionId(sessionId);

        for (Product productToMarkAsAvailable : productsToMarkAsAvailable) {
            productToMarkAsAvailable.setSessionId(null);
            repository.save(productToMarkAsAvailable);
        }
    }
}
