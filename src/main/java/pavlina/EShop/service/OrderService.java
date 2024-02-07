package pavlina.EShop.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import pavlina.EShop.domain.order.CreatedOrderDTO;
import pavlina.EShop.domain.order.Order;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.domain.product.ProductDTO;
import pavlina.EShop.exception_handling.exceptions.CartEmptyException;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.OrderNotFoundException;
import pavlina.EShop.repository.OrderRepository;

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

    public CreatedOrderDTO saveNewOrder(Order order, HttpSession session) {
        List<Product> productsInCart = cartService.getAllItemsInCart(session);
        if (productsInCart.isEmpty()) {
            throw new CartEmptyException();
        }
        List<ProductDTO> productDTOs = cartService.getAllItemsInCartDTO(session);
        order.setOrderedProducts(productsInCart);
        repository.save(order);
        productService.markProductsAsSold(order, productsInCart);
        cartService.clearTheCart(session);
        return new CreatedOrderDTO(productDTOs, order.getTotalPrice());
    }
}
