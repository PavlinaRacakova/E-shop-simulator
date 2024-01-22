package pavlina.EShop.controller;

import org.springframework.web.bind.annotation.*;
import pavlina.EShop.entities.order.Order;
import pavlina.EShop.entities.product.Product;
import pavlina.EShop.service.OrderService;
import pavlina.EShop.service.ProductService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class EShopController {

    private final ProductService productService;
    private final OrderService orderService;

    public EShopController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/all-products")
    public List<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/all-products-cheaper-than-{price}")
    public List<Product> findAllProductsCheaperThan(@PathVariable int price) {
        return productService.findAllProductsCheaperThan(price);
    }

    @GetMapping("/all-products-more-expensive-than-{price}")
    public List<Product> findAllProductsMoreExpensiveThan(@PathVariable int price) {
        return productService.findAllProductsMoreExpensiveThan(price);
    }

    @GetMapping("/all-orders")
    public List<Order> findAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/all-orders-made-on-{day}")
    public List<Order> findAllOrdersBySpecifiedLocalDate(@PathVariable LocalDate day) {
        return orderService.findByCreationDate(day);
    }
}
