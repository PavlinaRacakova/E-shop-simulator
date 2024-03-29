package pavlina.EShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pavlina.EShop.domain.product.Product;

import java.util.List;

/**
 * Product repository
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByPriceLessThanEqual(int price);

    List<Product> findByPriceGreaterThanEqual(int price);

    List<Product> findAllByOrderIsNullAndSessionIdIsNull();

    List<Product> findBySessionId(String sessionId);
}
