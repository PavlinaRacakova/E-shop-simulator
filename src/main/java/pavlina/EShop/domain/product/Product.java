package pavlina.EShop.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pavlina.EShop.domain.order.Order;

import java.util.Objects;

@Entity
@Table(name = "products")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NotEmpty
    @Length(max = 25, message = "Name of the product is too long")
    private String name;

    @Min(value = 1, message = "Price cannot be negative")
    @Max(value = 1_000_000, message = "Price cannot be higher than 1.000.000")
    private int price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(order, product.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, order);
    }
}
