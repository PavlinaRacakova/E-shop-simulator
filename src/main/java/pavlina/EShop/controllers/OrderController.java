package pavlina.EShop.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pavlina.EShop.entities.order.Order;
import pavlina.EShop.services.OrderService;

import java.time.LocalDate;

/**
 * Controller handling order related requests
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok().body(service.findAllOrders());
    }

    @GetMapping("/made-on-{day}")
    public ResponseEntity<?> getAllOrdersBySpecifiedLocalDate(@PathVariable LocalDate day) {
        return ResponseEntity.ok().body(service.findByCreationDate(day));
    }

    @PostMapping("/add")
    public ResponseEntity<?> createNewOrder(@RequestBody Order order, HttpSession session) {
        return service.saveNewOrder(order, session);
    }
}
