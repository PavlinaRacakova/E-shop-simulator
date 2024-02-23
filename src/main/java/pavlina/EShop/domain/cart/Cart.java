package pavlina.EShop.domain.cart;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.domain.product.ProductDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {

    private final List<Product> productsInCart;
    private final HttpSession httpSession;

    public Cart(HttpSession httpSession) {
        this.httpSession = httpSession;
        productsInCart = new ArrayList<>();
    }

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

    public List<ProductDTO> getProductsInCartAsDTO() {
        List<ProductDTO> productsDTO = new ArrayList<>();
        for(var product : productsInCart) {
            productsDTO.add(new ProductDTO(product.getName(), product.getPrice()));
        }
        return productsDTO;
    }
}
