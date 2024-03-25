package pavlina.EShop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pavlina.EShop.domain.order.CreatedOrderDTO;
import pavlina.EShop.domain.order.Order;
import pavlina.EShop.exception_handling.exceptions.ValidationException;
import pavlina.EShop.service.OrderService;

import java.net.URI;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {
        return ResponseEntity.ok().body(service.findById(id));
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
    public ResponseEntity<?> createNewOrder(@RequestBody @Valid Order order, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ValidationException(bindingResult));
        }
        CreatedOrderDTO createdOrder = service.saveNewOrder(order);
        return ResponseEntity.created(URI.create(
                        String.format("%s%s%s", ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(), "/orders/", order.getId())))
                .body(createdOrder);
    }
}
