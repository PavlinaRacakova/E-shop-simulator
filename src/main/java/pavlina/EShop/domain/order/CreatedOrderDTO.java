package pavlina.EShop.domain.order;

import pavlina.EShop.domain.product.ProductDTO;

import java.util.List;

public record CreatedOrderDTO(List<ProductDTO> orderedProducts, int totalPrice) {
}
