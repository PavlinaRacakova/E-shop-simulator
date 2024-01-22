package pavlina.EShop.service;

import org.springframework.stereotype.Service;
import pavlina.EShop.entities.order.Order;
import pavlina.EShop.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> findAllOrders() {
        return repository.findAll();
    }

    public List<Order> findByCreationDate(LocalDate localDate) {
        return repository.findByCreationDate(localDate);
    }
}
