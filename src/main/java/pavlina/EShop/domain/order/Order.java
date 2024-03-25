package pavlina.EShop.domain.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import pavlina.EShop.domain.customer.Customer;
import pavlina.EShop.domain.product.Product;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Embedded
    @Valid
    @AttributeOverrides({
            @AttributeOverride( name = "name", column = @Column(name = "customer_name")),
            @AttributeOverride( name = "address", column = @Column(name = "customer_address")),
            @AttributeOverride( name = "email", column = @Column(name = "customer_email")),
            @AttributeOverride( name = "phoneNumber", column = @Column(name = "customer_phone_number"))
    })
    private Customer customer;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate creationDate;

    @OneToMany(mappedBy = "order")
    private List<Product> orderedProducts;

    private int totalPrice;

    public Order() {
        creationDate = LocalDate.now();
    }

    public void setOrderedProducts(List<Product> orderedProducts) {
        this.orderedProducts = orderedProducts;
        totalPrice = orderedProducts.stream().mapToInt(Product::getPrice).sum();
    }
}
