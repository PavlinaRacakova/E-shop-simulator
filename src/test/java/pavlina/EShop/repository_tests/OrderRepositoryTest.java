package pavlina.EShop.repository_tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pavlina.EShop.domain.order.Order;
import pavlina.EShop.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repositoryUnderTest;

    @AfterEach
    void clearTheDatabase() {
        repositoryUnderTest.deleteAll();
    }

    @Test
    void whenOrderCreatedTodayIsPresent_thenReturnsListContainingThisOrder() {
        //arrange
        Order orderToInsert = new Order();
        List<Order> result;

        //act
        repositoryUnderTest.save(orderToInsert);
        result = repositoryUnderTest.findByCreationDate(LocalDate.now());

        //assert
        assertThat(result).containsExactly(orderToInsert);
    }

    @Test
    void whenOrderCreatedTodayIsNotPresent_thenReturnsEmptyList() {
        //arrange
        Order orderToInsert = new Order();
        List<Order> result;

        //act
        repositoryUnderTest.save(orderToInsert);
        result = repositoryUnderTest.findByCreationDate(LocalDate.now().minusDays(1));

        //assert
        assertThat(result).isEmpty();
    }
}