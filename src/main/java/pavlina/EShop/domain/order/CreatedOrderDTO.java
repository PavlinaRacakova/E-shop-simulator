package pavlina.EShop.domain.order;

import pavlina.EShop.domain.product.Product;

import java.util.List;

public record CreatedOrderDTO(List<Product> orderedProducts, int totalPrice) {
}
