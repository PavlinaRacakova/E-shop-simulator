package pavlina.EShop.service_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import pavlina.EShop.domain.order.Order;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;
import pavlina.EShop.repository.ProductRepository;
import pavlina.EShop.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Testing class for ProductService
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService serviceUnderTest;

    @Test
    public void whenAnyProductsAreInDatabase_thenReturnThemAsList() {
        //arrange
        Product firstProduct = new Product();
        Product secondProduct = new Product();
        List<Product> result;

        //act
        when(repository.findAll()).thenReturn(List.of(firstProduct, secondProduct));
        result = serviceUnderTest.findAllProducts();

        //assert
        assertThat(result).containsExactly(firstProduct, secondProduct);
    }

    @Test
    public void whenNoProductsAreInDatabase_thenThrowException() {
        //act
        when(repository.findAll()).thenReturn(List.of());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.findAllProducts())
                .isInstanceOf(DatabaseEmptyException.class);
    }

    @Test
    public void whenProductsWithCheaperPriceThanGivenArePresent_thenReturnThemAsList() {
        //arrange
        Product cheaperProduct = new Product();
        int priceToFind = 30;
        List<Product> result;

        //act
        when(repository.findByPriceLessThanEqual(priceToFind)).thenReturn(List.of(cheaperProduct));
        result = serviceUnderTest.findAllProductsCheaperThan(priceToFind);

        //assert
        assertThat(result).containsExactly(cheaperProduct);
    }

    @Test
    public void whenProductsWithCheaperPriceThanGivenAreNotPresent_thenThrowException() {
        //arrange
        int priceToFind = 30;

        //act
        when(repository.findByPriceLessThanEqual(priceToFind)).thenReturn(List.of());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.findAllProductsCheaperThan(priceToFind))
                .isInstanceOf(DatabaseEmptyException.class);
    }

    @Test
    public void whenProductsMoreExpensiveThanGivenArePresent_thenReturnThemAsList() {
        //arrange
        Product expensiveProduct = new Product();
        int priceToFind = 30;
        List<Product> result;

        //act
        when(repository.findByPriceGreaterThanEqual(priceToFind)).thenReturn(List.of(expensiveProduct));
        result = serviceUnderTest.findAllProductsMoreExpensiveThan(priceToFind);

        //assert
        assertThat(result).containsExactly(expensiveProduct);
    }

    @Test
    public void whenProductsMoreExpensiveThanGivenAreNotPresent_thenThrowException() {
        //arrange
        int priceToFind = 30;

        //act
        when(repository.findByPriceGreaterThanEqual(priceToFind)).thenReturn(List.of());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.findAllProductsMoreExpensiveThan(priceToFind))
                .isInstanceOf(DatabaseEmptyException.class);
    }

    @Test
    public void whenProductSpecifiedByIdIsPresent_thenReturnProduct() {
        //arrange
        Product product = new Product();
        int id = 1;
        Product result;

        //act
        product.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(product));
        result = serviceUnderTest.findProductById(id);

        //assert
        assertThat(result).isInstanceOf(Product.class)
                .isEqualTo(product);
    }

    @Test
    public void whenProductSpecifiedByIdIsNotPresent_thenThrowException() {
        //arrange
        int id = 1;

        //act
        when(repository.findById(id)).thenReturn(Optional.empty());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.findProductById(id))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void whenProductsThatHasBothOrderAndSessionIdNotNullArePresent_thenReturnThemAsList() {
        //arrange
        Product availableProduct = new Product();
        List<Product> result;

        //act
        when(repository.findAllByOrderIsNullAndSessionIdIsNull()).thenReturn(List.of(availableProduct));
        result = serviceUnderTest.findAllProductsThatArentSold();

        //assert
        assertThat(result).containsExactly(availableProduct);
    }

    @Test
    public void whenProductsThatHasBothOrderAndSessionIdNotNullAreNotPresent_thenThrowException() {
        //act
        when(repository.findAllByOrderIsNullAndSessionIdIsNull()).thenReturn(List.of());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.findAllProductsThatArentSold())
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void whenProductIsGiven_thenSaveIntoDatabase() {
        //arrange
        Product product = new Product();

        //act
        serviceUnderTest.saveNewProduct(product);

        //assert
        verify(repository).save(product);
    }

    @Test
    public void whenOrderedProductsAreGiven_thenSetItsOrderFieldToNotNullAndSaveThemIntoDatabase() {
        //arrange
        Order order = new Order();
        Product firstProduct = new Product();
        Product secondProduct = new Product();
        List<Product> productsToSell;

        //act
        productsToSell = List.of(firstProduct, secondProduct);
        serviceUnderTest.markProductsAsSold(order, productsToSell);

        //assert
        verify(repository).save(firstProduct);
        verify(repository).save(secondProduct);
        assertThat(firstProduct.getOrder()).isNotNull();
        assertThat(secondProduct.getOrder()).isNotNull();
    }

    @Test
    public void whenProductAndHttpSessionAreGiven_thenSetSessionIdToNotNull() {
        //arrange
        Product product = new Product();
        MockHttpSession httpSession = new MockHttpSession();

        //act
        serviceUnderTest.markProductAsReserved(product, httpSession);

        //assert
        assertThat(product.getSessionId()).isNotNull();
        verify(repository).save(product);
    }

    @Test
    public void whenSessionIdIsGiven_thenSetSessionIdToNullForAllProductsWithGivenSessionId() {
        //arrange
        Product product = new Product();
        String sessionId = "xy";

        //act
        product.setSessionId(sessionId);
        when(repository.findBySessionId(sessionId)).thenReturn(List.of(product));
        serviceUnderTest.releaseProductsForExpiredSession(sessionId);

        //assert
        verify(repository).save(product);
        assertThat(product.getSessionId()).isNull();
    }

    @Test
    public void whenProductIdIsGiven_thenSetThisProductSessionIdToNull() {
        //arrange
        Product product = new Product();
        int id = 1;
        String sessionId = "xy";

        //act
        product.setId(id);
        product.setSessionId(sessionId);
        when(repository.findById(id)).thenReturn(Optional.of(product));
        serviceUnderTest.markProductAsAvailableAgain(id);

        //assert
        assertThat(product.getSessionId()).isNull();
        verify(repository).save(product);
    }

    @Test
    public void whenInvalidProductIdIsGiven_thenThrowException() {
        //arrange
        int id = 1;

        //act
        when(repository.findById(id)).thenReturn(Optional.empty());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.markProductAsAvailableAgain(id))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
