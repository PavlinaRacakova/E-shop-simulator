package pavlina.EShop.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pavlina.EShop.domain.cart.Cart;
import pavlina.EShop.domain.cart.CartDTO;
import pavlina.EShop.domain.product.Product;
import pavlina.EShop.exception_handling.exceptions.ProductNotFoundException;

import java.util.List;

/**
 * Service for Cart controller
 */
@Service
public class CartService {

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public ResponseEntity<?> addToTheCart(int productId, HttpSession session) {
        Cart cart = getOrCreateCart(session);
        Product productToAdd = productService.findProductById(productId);
        if(productToAdd == null || !productToAdd.isAvailable()) {
            throw new ProductNotFoundException();
        }
        cart.addProduct(productToAdd);
        productService.markProductAsReserved(productToAdd, session);
        return ResponseEntity.ok().body(String.format("Product: %s added to the cart.", productToAdd.getName()));
    }

    public ResponseEntity<?> removeFromTheCart(int productId, HttpSession session) {
        Product productToRemove = productService.findProductById(productId);
        Cart cart = getOrCreateCart(session);
        if(productToRemove == null || !(cart.containsProduct(productToRemove))) {
            throw new ProductNotFoundException();
        }
        cart.removeProduct(productToRemove);
        return ResponseEntity.ok().body(String.format("Product: %s removed from the cart.", productToRemove.getName()));
    }

    public List<Product> getAllItemsInCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        return cart.getProductsInCart();
    }

    public CartDTO getAllItemsAndTheirPrice(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        return new CartDTO(cart.getProductsInCart(), cart.currentPriceOfProductsInCart());
    }

    public void clearTheCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.clearTheCart();
    }

    @Scheduled(fixedRate = 60000)
    public void clearTheCartDueToInactivity() {

    }
}
