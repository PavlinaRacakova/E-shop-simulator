package pavlina.EShop.entities.cart;

import lombok.Getter;
import pavlina.EShop.entities.product.Product;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Cart {

    private final List<Product> productsInCart = new ArrayList<>();

    public void addProduct(Product product) {
        productsInCart.add(product);
    }

    public void removeProduct(Product product) {
        productsInCart.remove(product);
    }

    public void clearTheCart() {
        productsInCart.clear();
    }

    public boolean containsProduct(Product product) {
        return productsInCart.contains(product);
    }
}
