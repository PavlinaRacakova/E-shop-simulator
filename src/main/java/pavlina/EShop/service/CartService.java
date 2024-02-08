package pavlina.EShop.service;

import jakarta.servlet.http.HttpSession;
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

    public void addToTheCart(int productId, HttpSession session) {
        Cart cart = getOrCreateCart(session);
        Product productToAdd = productService.findProductById(productId);
        if (productToAdd == null) {
            throw new ProductNotFoundException();
        } else if (!productToAdd.isAvailable()) {
            throw new ProductNotAvailableException();
        }
        cart.addProduct(productToAdd);
        productService.markProductAsReserved(productToAdd, session);
    }

    public void removeFromTheCart(int productId, HttpSession session) {
        Cart cart = getOrCreateCart(session);
        Product productToRemove = productService.findProductById(productId);
        if (productToRemove == null || !(cart.containsProduct(productToRemove))) {
            throw new ProductNotFoundException();
        }
        productService.markProductAsAvailableAgain(productId);
        cart.removeProduct(productToRemove);
    }

    public List<Product> getAllItemsInCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        if (cart.getProductsInCart().isEmpty()) {
            throw new CartEmptyException();
        }
        return cart.getProductsInCart();
    }

    public List<ProductDTO> getAllItemsInCartDTO(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        if (cart.getProductsInCart().isEmpty()) {
            throw new CartEmptyException();
        }
        return cart.getProductsInCartAsDTO();
    }

    public CartDTO getAllItemsAndTheirPriceDTO(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        return new CartDTO(cart.getProductsInCartAsDTO(), cart.currentPriceOfProductsInCart());
    }

    public void clearTheCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.clearTheCart();
    }
}
