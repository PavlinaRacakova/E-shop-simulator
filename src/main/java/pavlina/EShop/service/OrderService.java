package pavlina.EShop.service;

import org.springframework.stereotype.Service;
import pavlina.EShop.entities.order.Order;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.OrderNotFoundException;
import pavlina.EShop.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Order service class
 */
@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
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
}
