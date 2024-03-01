package pavlina.EShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pavlina.EShop.domain.order.Order;
import pavlina.EShop.domain.product.Product;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repositoryUnderTest;

    @Autowired
    private OrderRepository orderRepository;

    @AfterEach
    void clearTheDatabases() {
        repositoryUnderTest.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void whenProductsWithSmallerPriceThenGivenArePresent_thenReturnThemAsList() {
        //arrange
        Product productWithSmallerPrice = new Product();
        Product productWithHigherPrice = new Product();
        int smallerPrice = 20;
        int higherPrice = 40;
        int priceToFind = 30;
        List<Product> result;

        //act
        productWithSmallerPrice.setPrice(smallerPrice);
        productWithHigherPrice.setPrice(higherPrice);
        setMockNamesOfProducts(productWithSmallerPrice, productWithHigherPrice);
        repositoryUnderTest.save(productWithSmallerPrice);
        repositoryUnderTest.save(productWithHigherPrice);
        result = repositoryUnderTest.findByPriceLessThanEqual(priceToFind);

        //assert
        assertThat(result).containsExactly(productWithSmallerPrice);
    }

    @Test
    void whenProductsWithSmallerPriceThenGivenAreNotPresent_thenReturnEmptyList() {
        //arrange
        Product productWithHigherPrice = new Product();
        int higherPrice = 40;
        int priceToFind = 30;
        List<Product> result;

        //act
        productWithHigherPrice.setPrice(higherPrice);
        setMockNamesOfProducts(productWithHigherPrice);
        repositoryUnderTest.save(productWithHigherPrice);
        result = repositoryUnderTest.findByPriceLessThanEqual(priceToFind);

        //assert
        assertThat(result).isEmpty();
    }

    @Test
    void whenProductsWithHigherPriceThenGivenArePresent_thenReturnThemAsList() {
        //arrange
        Product productWithSmallerPrice = new Product();
        Product productWithHigherPrice = new Product();
        int smallerPrice = 20;
        int higherPrice = 40;
        int priceToFind = 30;
        List<Product> result;

        //act
        productWithSmallerPrice.setPrice(smallerPrice);
        productWithHigherPrice.setPrice(higherPrice);
        setMockNamesOfProducts(productWithSmallerPrice, productWithHigherPrice);
        repositoryUnderTest.save(productWithSmallerPrice);
        repositoryUnderTest.save(productWithHigherPrice);
        result = repositoryUnderTest.findByPriceGreaterThanEqual(priceToFind);

        //assert
        assertThat(result).containsExactly(productWithHigherPrice);
    }

    @Test
    void whenProductsWithHigherPriceThenGivenAreNotPresent_thenReturnEmptyList() {
        //arrange
        Product productWithSmallerPrice = new Product();
        int smallerPrice = 20;
        int priceToFind = 30;
        List<Product> result;

        //act
        productWithSmallerPrice.setPrice(smallerPrice);
        setMockNamesOfProducts(productWithSmallerPrice);
        repositoryUnderTest.save(productWithSmallerPrice);
        result = repositoryUnderTest.findByPriceGreaterThanEqual(priceToFind);

        //assert
        assertThat(result).isEmpty();
    }

    @Test
    void whenProductsWithBothOrderAndIdSessionAsNullArePresent_thenReturnThemAsList() {
        //arrange
        Product productWithOrderNotNull = new Product();
        Product productWithSessionIdNotNull = new Product();
        Product productWithBothNull = new Product();
        Order mockOrder = new Order();
        String mockSessionId = "xy";
        List<Product> result;

        //act
        setMockNamesOfProducts(productWithOrderNotNull, productWithSessionIdNotNull, productWithBothNull);
        orderRepository.save(mockOrder);
        productWithOrderNotNull.setOrder(mockOrder);
        productWithSessionIdNotNull.setSessionId(mockSessionId);
        repositoryUnderTest.save(productWithOrderNotNull);
        repositoryUnderTest.save(productWithSessionIdNotNull);
        repositoryUnderTest.save(productWithBothNull);
        result = repositoryUnderTest.findAllByOrderIsNullAndSessionIdIsNull();

        //assert
        assertThat(result).containsExactly(productWithBothNull);
    }

    @Test
    void whenProductsWithBothOrderAndIdSessionAsNullAreNotPresent_thenReturnEmptyList() {
        //arrange
        Product productWithOrderNotNull = new Product();
        Product productWithSessionIdNotNull = new Product();
        Order mockOrder = new Order();
        String mockSessionId = "xy";
        List<Product> result;

        //act
        setMockNamesOfProducts(productWithOrderNotNull, productWithSessionIdNotNull);
        orderRepository.save(mockOrder);
        productWithOrderNotNull.setOrder(mockOrder);
        productWithSessionIdNotNull.setSessionId(mockSessionId);
        repositoryUnderTest.save(productWithOrderNotNull);
        repositoryUnderTest.save(productWithSessionIdNotNull);
        result = repositoryUnderTest.findAllByOrderIsNullAndSessionIdIsNull();

        //assert
        assertThat(result).isEmpty();
    }

    @Test
    void whenProductsWithGivenSessionIdArePresent_thenReturnThemAsList() {
        //arrange
        Product productWithGivenSessionId = new Product();
        Product productWithNullInsteadOfSessionId = new Product();
        String mockSessionId = "xy";
        List<Product> result;

        //act
        setMockNamesOfProducts(productWithGivenSessionId, productWithNullInsteadOfSessionId);
        productWithGivenSessionId.setSessionId(mockSessionId);
        repositoryUnderTest.save(productWithGivenSessionId);
        repositoryUnderTest.save(productWithNullInsteadOfSessionId);
        result = repositoryUnderTest.findBySessionId(mockSessionId);

        //assert
        assertThat(result).containsExactly(productWithGivenSessionId);
    }

    @Test
    void whenProductsWithGivenSessionIdAreNotPresent_thenReturnEmptyList() {
        //arrange
        Product productWithNullInsteadOfSessionId = new Product();
        String mockSessionId = "xy";
        List<Product> result;

        //act
        setMockNamesOfProducts(productWithNullInsteadOfSessionId);
        repositoryUnderTest.save(productWithNullInsteadOfSessionId);
        result = repositoryUnderTest.findBySessionId(mockSessionId);

        //assert
        assertThat(result).isEmpty();
    }

    private void setMockNamesOfProducts(Product... products) {
        Arrays.stream(products).forEach(product -> product.setName("mock name"));
    }
}