package pavlina.EShop.domain.cart;

import lombok.Getter;
import pavlina.EShop.domain.product.Product;

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

    public int currentPriceOfProductsInCart() {
        return productsInCart.stream().mapToInt(Product::getPrice).sum();
    }
}
