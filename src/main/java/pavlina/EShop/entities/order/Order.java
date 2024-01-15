package pavlina.EShop.entities.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pavlina.EShop.entities.customer.Customer;
import pavlina.EShop.entities.product.Product;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
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

    public Order(Customer customer, List<Product> orderedProducts) {
        this.customer = customer;
        this.orderedProducts = orderedProducts;
        creationDate = LocalDate.now();
        totalPrice = orderedProducts.stream().mapToInt(Product::getPrice).sum();
    }
}
