package pavlina.EShop.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pavlina.EShop.entities.cart.Cart;
import pavlina.EShop.entities.product.Product;
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
        Product productToAdd = productService.findProductById(productId);
        if(productToAdd == null || productToAdd.getOrder() != null) {
            throw new ProductNotFoundException();
        }
        Cart cart = getOrCreateCart(session);
        cart.addProduct(productToAdd);
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

    public void clearTheCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.clearTheCart();
    }
}
