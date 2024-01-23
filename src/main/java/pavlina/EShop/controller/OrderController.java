package pavlina.EShop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pavlina.EShop.service.OrderService;

import java.time.LocalDate;

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
}
