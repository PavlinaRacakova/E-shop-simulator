package pavlina.EShop.service;

import org.springframework.stereotype.Service;
import pavlina.EShop.domain.cart.Cart;
import pavlina.EShop.domain.cart.CartDTO;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.domain.product.ProductDTO;
import pavlina.EShop.exception_handling.exceptions.CartEmptyException;
import pavlina.EShop.exception_handling.exceptions.ProductNotAvailableException;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;

import java.util.List;

/**
 * Service for Cart controller
 */
@Service
public class CartService {

    private final ProductService productService;
    private final Cart cart;

    public CartService(ProductService productService, Cart cart) {
        this.productService = productService;
        this.cart = cart;
    }

    public void addToTheCart(int productId) {
        Product productToAdd = productService.findProductById(productId);
        if (productToAdd == null) {
            throw new ProductNotFoundException();
        } else if (!productToAdd.isAvailable()) {
            throw new ProductNotAvailableException();
        }
        cart.addProduct(productToAdd);
        productService.markProductAsReserved(productToAdd, cart.getHttpSession());
    }

    public void removeFromTheCart(int productId) {
        Product productToRemove = productService.findProductById(productId);
        if (productToRemove == null || !(cart.containsProduct(productToRemove))) {
            throw new ProductNotFoundException();
        }
        productService.markProductAsAvailableAgain(productId);
        cart.removeProduct(productToRemove);
    }

    public List<Product> getAllItemsInCart() {
        if (cart.getProductsInCart().isEmpty()) {
            throw new CartEmptyException();
        }
        return cart.getProductsInCart();
    }

    public List<ProductDTO> getAllItemsInCartDTO() {
        if (cart.getProductsInCart().isEmpty()) {
            throw new CartEmptyException();
        }
        return cart.getProductsInCartAsDTO();
    }

    public CartDTO getAllItemsAndTheirPriceDTO() {
        return new CartDTO(cart.getProductsInCartAsDTO(), cart.currentPriceOfProductsInCart());
    }

    public void clearTheCart() {
        cart.clearTheCart();
    }
}
