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
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.exception_handling.ErrorMessage;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;
import pavlina.EShop.service.ProductService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Test class for ProductController.class
 */

@AutoConfigureJsonTesters
@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    private Product product;

    private List<Product> productsList;

    private ErrorMessage errorMessage;

    @BeforeEach
    public void setup() {
        product = new Product();
        product.setId(1);
        product.setName("product");
        product.setPrice(10);
        productsList = List.of(product);
        errorMessage = new ErrorMessage(null);
    }

    @Test
    public void whenAnyProductsAreStoredInDatabase_thenReturnThemInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        given(service.findAllProducts()).willReturn(productsList);
        result = mockMvc.perform(
                        get("/products/all")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(productsList));
    }

    @Test
    public void whenNoProductsAreStoredInDatabase_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        DatabaseEmptyException databaseEmptyException = new DatabaseEmptyException();

        //act
        errorMessage = new ErrorMessage(databaseEmptyException.getMessage());
        given(service.findAllProducts()).willThrow(databaseEmptyException);
        result = mockMvc.perform(
                        get("/products/all")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenAnyProductsAreAvailable_thenReturnThemInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        given(service.findAllProductsThatArentSold()).willReturn(productsList);
        result = mockMvc.perform(
                        get("/products/available")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(productsList));
    }

    @Test
    public void whenNoProductsAreAvailable_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        ProductNotFoundException productNotFoundException = new ProductNotFoundException();

        //act
        errorMessage = new ErrorMessage(productNotFoundException.getMessage());
        given(service.findAllProductsThatArentSold()).willThrow(productNotFoundException);
        result = mockMvc.perform(
                        get("/products/available")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenAnyCheaperProductsThanGivenPriceAreStoredInDatabase_thenReturnThemInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        given(service.findAllProductsCheaperThan(product.getPrice())).willReturn(productsList);
        result = mockMvc.perform(
                        get(String.format("/products/cheaper-than-%d", product.getPrice()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(productsList));
    }

    @Test
    public void whenNoProductsAreCheaperThanGivenPrice_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        ProductNotFoundException productNotFoundException = new ProductNotFoundException();

        //act
        errorMessage = new ErrorMessage(productNotFoundException.getMessage());
        given(service.findAllProductsCheaperThan(product.getPrice())).willThrow(productNotFoundException);
        result = mockMvc.perform(
                        get(String.format("/products/cheaper-than-%d", product.getPrice()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenAnyProductsMoreExpensiveThanGivenPriceAreStoredInDatabase_thenReturnThemInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        given(service.findAllProductsMoreExpensiveThan(product.getPrice())).willReturn(productsList);
        result = mockMvc.perform(
                        get(String.format("/products/more-expensive-than-%d", product.getPrice()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(productsList));
    }

    @Test
    public void whenNoProductsAreMoreExpensiveThanGivenPrice_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        ProductNotFoundException productNotFoundException = new ProductNotFoundException();

        //act
        errorMessage = new ErrorMessage(productNotFoundException.getMessage());
        given(service.findAllProductsMoreExpensiveThan(product.getPrice())).willThrow(productNotFoundException);
        result = mockMvc.perform(
                        get(String.format("/products/more-expensive-than-%d", product.getPrice()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenProductWithGivenIdIsStoredInDatabase_thenReturnItInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        given(service.findProductById(product.getId())).willReturn(product);
        result = mockMvc.perform(
                        get(String.format("/products/%d", product.getId()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(product));
    }

    @Test
    public void whenProductWithGivenIdIsNotStoredInDatabase_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        ProductNotFoundException productNotFoundException = new ProductNotFoundException();

        //act
        errorMessage = new ErrorMessage(productNotFoundException.getMessage());
        given(service.findProductById(product.getId())).willThrow(productNotFoundException);
        result = mockMvc.perform(
                        get(String.format("/products/%d", product.getId()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenNewValidProductIsGivenToBeStoredInDatabase_thenCallMethodToSaveItAndReturnCreatedStatus() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        result = mockMvc.perform(
                        post("/products/add").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                .andReturn().getResponse();

        //assert
        verify(service).saveNewProduct(product);
        assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void whenNewInvalidProductIsGivenToBeStoredInDatabase_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        product.setPrice(-7);
        result = mockMvc.perform(
                        post("/products/add").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                .andReturn().getResponse();

        //assert
        verify(service, never()).saveNewProduct(product);
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getContentAsString())
                .isEqualTo("{\"validation_errors\":{\"price\":\"Price cannot be negative\"}}");
    }
}