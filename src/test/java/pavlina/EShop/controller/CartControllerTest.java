package pavlina.EShop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import pavlina.EShop.domain.cart.CartDTO;
import pavlina.EShop.domain.product.ProductDTO;
import pavlina.EShop.exception_handling.ErrorMessage;
import pavlina.EShop.exception_handling.exceptions.CartEmptyException;
import pavlina.EShop.exception_handling.exceptions.ProductNotAvailableException;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;
import pavlina.EShop.service.CartService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Test class for CartController
 */

@AutoConfigureJsonTesters
@WebMvcTest(controllers = CartController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService service;

    private ErrorMessage errorMessage;

    @BeforeEach
    public void setup() {
        errorMessage = new ErrorMessage(null);
    }

    @Test
    public void whenProductWithGivenIdIsStoredInDatabaseAndIsAvailableToBePlacedIntoCart_thenReturnOkStatus() throws Exception {
        //arrange
        MockHttpServletResponse result;
        int id = 1;
        ProductDTO productDTO = new ProductDTO("mockName", 2);

        //act
        given(service.addToTheCart(id)).willReturn(productDTO);
        result = mockMvc.perform(
                        post(String.format("/cart/add/%d", id))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(productDTO));
    }

    @Test
    public void whenProductWithGivenIdIsNotAvailableToBePlacedIntoCart_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        int id = 1;
        ProductNotAvailableException productNotAvailableException = new ProductNotAvailableException();

        //act
        errorMessage = new ErrorMessage(productNotAvailableException.getMessage());
        given(service.addToTheCart(id)).willThrow(productNotAvailableException);
        result = mockMvc.perform(
                        post(String.format("/cart/add/%d", id))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenProductWithGivenIdIsNotStoredInDatabaseAndThereforeCannotBePlacedIntoCart_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        int id = 1;
        ProductNotFoundException productNotFoundException = new ProductNotFoundException();

        //act
        errorMessage = new ErrorMessage(productNotFoundException.getMessage());
        given(service.addToTheCart(id)).willThrow(productNotFoundException);
        result = mockMvc.perform(
                        post(String.format("/cart/add/%d", id))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenProductWithGivenIdIsCurrentlyInCartAndCanBeRemoved_thenReturnOkStatus() throws Exception {
        //arrange
        MockHttpServletResponse result;
        int id = 1;
        ProductDTO productDTO = new ProductDTO("mockName", 2);

        //act
        given(service.removeFromTheCart(id)).willReturn(productDTO);
        result = mockMvc.perform(
                        delete(String.format("/cart/delete/%d", id))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(productDTO));
    }

    @Test
    public void whenProductWithGivenIdIsNotPlacedInCartOrIsNonexistentAndThereforeCannotBeRemoved_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        int id = 1;
        ProductNotFoundException productNotFoundException = new ProductNotFoundException();

        //act
        errorMessage = new ErrorMessage(productNotFoundException.getMessage());
        given(service.removeFromTheCart(id)).willThrow(productNotFoundException);
        result = mockMvc.perform(
                        delete(String.format("/cart/delete/%d", id))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenAnyProductsArePlacedInsideACart_thenReturnCartAsDTO() throws Exception {
        //arrange
        MockHttpServletResponse result;
        ProductDTO productDTO = new ProductDTO("mockName", 2);
        CartDTO cartDTO = new CartDTO(List.of(productDTO), 2);

        //act
        given(service.getAllItemsAndTheirPriceDTO()).willReturn(cartDTO);
        result = mockMvc.perform(
                        get("/cart/items")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(cartDTO));
    }

    @Test
    public void whenNoProductsArePlacedInsideACart_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        CartEmptyException cartEmptyException = new CartEmptyException();

        //act
        errorMessage = new ErrorMessage(cartEmptyException.getMessage());
        given(service.getAllItemsAndTheirPriceDTO()).willThrow(cartEmptyException);
        result = mockMvc.perform(
                        get("/cart/items")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }
}
