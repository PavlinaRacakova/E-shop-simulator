package pavlina.EShop.service_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pavlina.EShop.domain.cart.Cart;
import pavlina.EShop.domain.cart.CartDTO;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.domain.product.ProductDTO;
import pavlina.EShop.exception_handling.exceptions.CartEmptyException;
import pavlina.EShop.exception_handling.exceptions.ProductNotAvailableException;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;
import pavlina.EShop.service.CartService;
import pavlina.EShop.service.ProductService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing class for CartService
 */
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private Cart cart;

    @InjectMocks
    private CartService serviceUnderTest;

    @Test
    public void whenValidProductIdIsGiven_thenAddProductIntoCartAndCallMethodToMarkProductAsReserved() {
        //arrange
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        Product product = new Product();
        int id = 1;

        //act
        product.setId(id);
        when(productService.findProductById(id)).thenReturn(product);
        serviceUnderTest.addToTheCart(id);

        //assert
        verify(cart).addProduct(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId())
                .isEqualTo(id);
        verify(productService).markProductAsReserved(product, cart.getHttpSession());
    }

    @Test
    public void whenNonAvailableProductsIdIsGiven_thenThrowException() {
        //arrange
        Product product = new Product();
        int id = 2;
        String sessionId = "xy";

        //act
        product.setSessionId(sessionId);
        when(productService.findProductById(id)).thenReturn(product);

        //assert
        assertThatThrownBy(() -> serviceUnderTest.addToTheCart(id))
                .isInstanceOf(ProductNotAvailableException.class);
    }

    @Test
    public void whenIdOfProductInCartIsGiven_thenRemoveProductFromCartAndCallMethodToMarkProductAsAvailableAgain() {
        //arrange
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        Product productToRemove = new Product();
        int id = 1;

        //act
        productToRemove.setId(id);
        when(productService.findProductById(id)).thenReturn(productToRemove);
        when(cart.containsProduct(productToRemove)).thenReturn(true);
        serviceUnderTest.removeFromTheCart(id);

        //assert
        verify(productService).markProductAsAvailableAgain(productToRemove.getId());
        verify(cart).removeProduct(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId())
                .isEqualTo(id);
    }

    @Test
    public void whenIdOfProductNotPresentInCartIsGiven_thenThrowException() {
        //arrange
        Product productToRemove = new Product();
        int id = 1;

        //act
        when(productService.findProductById(id)).thenReturn(productToRemove);
        when(cart.containsProduct(productToRemove)).thenReturn(false);

        //assert
        assertThatThrownBy(() -> serviceUnderTest.removeFromTheCart(id))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void whenAnyProductsArePresentInCart_thenReturnThemAsList() {
        //arrange
        Product productInCart = new Product();
        List<Product> result;

        //act
        when(cart.getProductsInCart()).thenReturn(List.of(productInCart));
        result = serviceUnderTest.getAllItemsInCart();

        //assert
        assertThat(result).containsExactly(productInCart);
    }

    @Test
    public void whenNoProductsArePresentInCart_thenThrowException() {
        //act
        when(cart.getProductsInCart()).thenReturn(List.of());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.getAllItemsInCart())
                .isInstanceOf(CartEmptyException.class);
    }

    @Test
    public void whenAnyProductsArePresentInCart_thenReturnThemAsDtoObject() {
        //arrange
        Product productInCart = new Product();
        List<Product> result;

        //act
        when(cart.getProductsInCart()).thenReturn(List.of(productInCart));
        result = serviceUnderTest.getAllItemsInCart();

        //assert
        assertThat(result).containsExactly(productInCart);
    }

    @Test
    public void whenAnyProductsArePresentInCart_thenReturnThemAsCartDTO() {
        //arrange
        ProductDTO firstProductInCartAsDTO = new ProductDTO("name", 10);
        ProductDTO secondProductInCartAsDTO = new ProductDTO("name", 20);
        int priceOfAllProducts = 30;
        CartDTO result;

        //act
        when(cart.getProductsInCartAsDTO()).thenReturn(List.of(firstProductInCartAsDTO, secondProductInCartAsDTO));
        when(cart.currentPriceOfProductsInCart()).thenReturn(priceOfAllProducts);
        result = serviceUnderTest.getAllItemsAndTheirPriceDTO();

        //assert
        assertThat(result.products()).hasSize(2);
        assertThat(result.price()).isEqualTo(priceOfAllProducts);
    }

    @Test
    public void whenCartIsEmptyAndThereforeCartDtoCannotBeCreated_thenThrowException() {
        //act
        when(cart.getProductsInCartAsDTO()).thenReturn(List.of());

        //assert
        assertThatThrownBy(() -> serviceUnderTest.getAllItemsAndTheirPriceDTO())
                .isInstanceOf(CartEmptyException.class);
    }

    @Test
    public void whenIsInvoked_thenRemoveAllProductsFromCartAndReturnThemAsList() {
        //arrange
        ProductDTO productInCartAsDTO = new ProductDTO("name", 10);
        List<ProductDTO> result;

        //act
        when(cart.getProductsInCartAsDTO()).thenReturn(List.of(productInCartAsDTO));
        result = serviceUnderTest.clearTheCartDueToFinishedOrder();

        //assert
        verify(cart).clearTheCart();
        assertThat(result).containsExactly(productInCartAsDTO);
    }
}
