package pavlina.EShop.entities.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pavlina.EShop.entities.order.Order;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    private int price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
