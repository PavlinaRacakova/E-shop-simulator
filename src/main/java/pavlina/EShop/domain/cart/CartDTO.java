package pavlina.EShop.domain.cart;

import pavlina.EShop.domain.product.ProductDTO;

import java.util.List;

public record CartDTO (List<ProductDTO> products, int price){
}
