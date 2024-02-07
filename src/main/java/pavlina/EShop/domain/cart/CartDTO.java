package pavlina.EShop.domain.cart;

import pavlina.EShop.domain.product.Product;

import java.util.List;

public record CartDTO (List<Product> products, int price){
}
