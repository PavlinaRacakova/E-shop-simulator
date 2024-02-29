package pavlina.EShop.service_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pavlina.EShop.domain.order.CreatedOrderDTO;
import pavlina.EShop.domain.order.Order;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.domain.product.ProductDTO;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.OrderNotFoundException;
import pavlina.EShop.repository.OrderRepository;
import pavlina.EShop.service.CartService;
import pavlina.EShop.service.OrderService;
import pavlina.EShop.service.ProductService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing class for OrderService
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService serviceUnderTest;

    @Test
    void whenAnyOrdersArePresent_thenReturnThemAsList() {
        //arrange
        Order order = new Order();
        List<Order> result;

        //act
        when(repository.findAll()).thenReturn(List.of(order));
        result = serviceUnderTest.findAllOrders();

        //assert
        assertThat(result).containsExactly(order);
    }

    @Test
    void whenNoOrdersArePresent_thenThrowException() {
        //act
        when(repository.findAll()).thenReturn(List.of());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.findAllOrders())
                .isInstanceOf(DatabaseEmptyException.class);
    }

    @Test
    void whenAnyOrdersCreatedOnGivenDayArePresent_thenReturnThemAsList() {
        //arrange
        Order order = new Order();
        List<Order> result;

        //act
        when(repository.findByCreationDate(LocalDate.now())).thenReturn(List.of(order));
        result = serviceUnderTest.findByCreationDate(LocalDate.now());

        //assert
        assertThat(result).containsExactly(order);
    }

    @Test
    void whenNoOrdersCreatedOnGivenDayArePresent_thenThrowException() {
        //act
        when(repository.findByCreationDate(LocalDate.now())).thenReturn(List.of());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.findByCreationDate(LocalDate.now()))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void whenValidOrderRequestIsGiven_thenSaveNewOrderIntoDatabaseAndCallMethodToMarkProductsAsSold() {
        //arrange
        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        Order order = new Order();
        Product firstProduct = new Product();
        Product secondProduct = new Product();
        ProductDTO firstProductDTO = new ProductDTO("first product", 30);
        ProductDTO secondProductDTO = new ProductDTO("second product", 20);
        List<Product> productsToBuy;
        CreatedOrderDTO result;

        //act
        firstProduct.setPrice(30);
        secondProduct.setPrice(20);
        productsToBuy = List.of(firstProduct, secondProduct);

        when(cartService.getAllItemsInCart()).thenReturn(productsToBuy);
        when(cartService.clearTheCartDueToFinishedOrder())
                .thenReturn(List.of(firstProductDTO, secondProductDTO));
        result = serviceUnderTest.saveNewOrder(order);

        //assert
        verify(repository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getOrderedProducts())
                .containsExactly(firstProduct, secondProduct);
        verify(productService).markProductsAsSold(order, productsToBuy);
        verify(cartService).clearTheCartDueToFinishedOrder();
        assertThat(result.orderedProducts()).hasSize(2);
        assertThat(result.totalPrice()).isEqualTo(50);
    }
}