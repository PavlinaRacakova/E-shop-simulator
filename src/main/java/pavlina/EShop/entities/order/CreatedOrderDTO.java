package pavlina.EShop.entities.order;

import lombok.Getter;
import lombok.Setter;
import pavlina.EShop.entities.product.Product;

import java.util.List;

@Getter
@Setter
public class CreatedOrderDTO {

    private List<Product> orderedProducts;
    private int totalPrice;

    public CreatedOrderDTO(List<Product> orderedProducts, int totalPrice) {
        this.orderedProducts = orderedProducts;
        this.totalPrice = totalPrice;
    }
}
