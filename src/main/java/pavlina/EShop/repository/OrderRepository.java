package pavlina.EShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pavlina.EShop.entities.order.Order;

import java.time.LocalDate;
import java.util.List;

/**
 * Order repository
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCreationDate(LocalDate creationDate);
}
