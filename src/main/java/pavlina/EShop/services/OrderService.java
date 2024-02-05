package pavlina.EShop.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pavlina.EShop.entities.order.CreatedOrderDTO;
import pavlina.EShop.entities.order.Order;
import pavlina.EShop.entities.product.Product;
import pavlina.EShop.exception_handling.exceptions.CartEmptyException;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.OrderNotFoundException;
import pavlina.EShop.repositories.OrderRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for order controller
 */
@Service
public class OrderService {

    private final OrderRepository repository;
    private final CartService cartService;
    private final ProductService productService;

    public OrderService(OrderRepository repository, CartService cartService, ProductService productService) {
        this.repository = repository;
        this.cartService = cartService;
        this.productService = productService;
    }

    public List<Order> findAllOrders() {
        List<Order> orders = repository.findAll();
        if (!orders.isEmpty()) {
            return orders;
        } else {
            throw new DatabaseEmptyException();
        }
    }

    public List<Order> findByCreationDate(LocalDate localDate) {
        List<Order> orders = repository.findByCreationDate(localDate);
        if (!orders.isEmpty()) {
            return orders;
        } else {
            throw new OrderNotFoundException();
        }
    }

    public ResponseEntity<?> saveNewOrder(Order order, HttpSession session) {
        List<Product> productsInCart = cartService.getAllItemsInCart(session);
        if(productsInCart.isEmpty()) {
            throw new CartEmptyException();
        }
        order.setOrderedProducts(productsInCart);
        repository.save(order);
        productService.markProductsAsSold(order, productsInCart);
        cartService.clearTheCart(session);
        return ResponseEntity.ok().body(new CreatedOrderDTO(order.getOrderedProducts(), order.getTotalPrice()));
    }
}
