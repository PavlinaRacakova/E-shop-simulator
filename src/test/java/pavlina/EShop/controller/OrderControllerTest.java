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
import pavlina.EShop.domain.order.CreatedOrderDTO;
import pavlina.EShop.domain.order.Order;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.domain.product.ProductDTO;
import pavlina.EShop.exception_handling.ErrorMessage;
import pavlina.EShop.exception_handling.exceptions.CartEmptyException;
import pavlina.EShop.exception_handling.exceptions.DatabaseEmptyException;
import pavlina.EShop.exception_handling.exceptions.OrderNotFoundException;
import pavlina.EShop.service.OrderService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Test class for OrderController
 */

@AutoConfigureJsonTesters
@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService service;

    private Order order;

    private List<Order> ordersList;

    private ErrorMessage errorMessage;

    @BeforeEach
    public void setup() {
        order = new Order();
        ordersList = List.of(order);
        errorMessage = new ErrorMessage(null);
    }

    @Test
    public void whenOrderWithGivenIdIsStoredInDatabase_thenReturnItInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;
        int id = 1;

        //act
        given(service.findById(id)).willReturn(order);
        result = mockMvc.perform(
                        get(String.format("/orders/%d", id))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(order));
    }

    @Test
    public void whenOrderWithGivenIdIsNotStoredInDatabase_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        int id = 1;
        OrderNotFoundException orderNotFoundException = new OrderNotFoundException();

        //act
        errorMessage = new ErrorMessage(orderNotFoundException.getMessage());
        given(service.findById(id)).willThrow(orderNotFoundException);
        result = mockMvc.perform(
                        get(String.format("/orders/%d", id))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenAnyOrdersArePresentInDatabase_thenReturnThemInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        given(service.findAllOrders()).willReturn(ordersList);
        result = mockMvc.perform(
                        get("/orders/all")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(ordersList));
    }

    @Test
    public void whenNoOrdersAreStoredInDatabase_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        DatabaseEmptyException databaseEmptyException = new DatabaseEmptyException();

        //act
        errorMessage = new ErrorMessage(databaseEmptyException.getMessage());
        given(service.findAllOrders()).willThrow(databaseEmptyException);
        result = mockMvc.perform(
                        get("/orders/all")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenAnyOrdersMadeOnGivenDayArePresentInDatabase_thenReturnThemInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;

        //act
        given(service.findByCreationDate(order.getCreationDate())).willReturn(ordersList);
        result = mockMvc.perform(
                        get(String.format("/orders/made-on-%s", order.getCreationDate().toString()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(ordersList));
    }

    @Test
    public void whenNoOrdersMadeOnGivenDayAreStoredInDatabase_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        OrderNotFoundException orderNotFoundException = new OrderNotFoundException();

        //act
        errorMessage = new ErrorMessage(orderNotFoundException.getMessage());
        given(service.findByCreationDate(order.getCreationDate().minusDays(1))).willThrow(orderNotFoundException);
        result = mockMvc.perform(
                        get(String.format("/orders/made-on-%s", order.getCreationDate().minusDays(1)))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }

    @Test
    public void whenValidOrderIsGivenToSaveIntoDatabase_thenReturnSavedOrderInBody() throws Exception {
        //arrange
        MockHttpServletResponse result;
        CreatedOrderDTO createdOrderDTO = new CreatedOrderDTO(List.of(new ProductDTO("product", 5)), 5);

        //act
        order.setOrderedProducts(List.of(new Product()));
        given(service.saveNewOrder(any(Order.class))).willReturn(createdOrderDTO);
        result = mockMvc.perform(
                        post("/orders/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(order)))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(createdOrderDTO));
    }

    @Test
    public void whenInvalidOrdersIsGivenToSaveIntoDatabase_thenReturnErrorMessage() throws Exception {
        //arrange
        MockHttpServletResponse result;
        CartEmptyException cartEmptyException = new CartEmptyException();

        //act
        order.setOrderedProducts(List.of(new Product()));
        errorMessage = new ErrorMessage(cartEmptyException.getMessage());
        given(service.saveNewOrder(any(Order.class))).willThrow(cartEmptyException);
        result = mockMvc.perform(
                        post("/orders/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(order)))
                .andReturn().getResponse();

        //assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(result.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(errorMessage));
    }
}
