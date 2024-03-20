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

    public ProductDTO addToTheCart(int productId) {
        Product productToAdd = productService.findProductById(productId);
        if (!productToAdd.isAvailable()) {
            throw new ProductNotAvailableException();
        }
        cart.addProduct(productToAdd);
        productService.markProductAsReserved(productToAdd, cart.getHttpSession());
        return new ProductDTO(productToAdd.getName(), productToAdd.getPrice());
    }

    public ProductDTO removeFromTheCart(int productId) {
        Product productToRemove = productService.findProductById(productId);
        if (!cart.containsProduct(productToRemove)) {
            throw new ProductNotFoundException();
        }
        productService.markProductAsAvailableAgain(productId);
        cart.removeProduct(productToRemove);
        return new ProductDTO(productToRemove.getName(), productToRemove.getPrice());
    }

    public List<Product> getAllItemsInCart() {
        if (cart.getProductsInCart().isEmpty()) {
            throw new CartEmptyException();
        }
        return cart.getProductsInCart();
    }

    public CartDTO getAllItemsAndTheirPriceDTO() {
        List<ProductDTO> productDTOs = cart.getProductsInCartAsDTO();
        if (productDTOs.isEmpty()) {
            throw new CartEmptyException();
        }
        return new CartDTO(productDTOs, cart.currentPriceOfProductsInCart());
    }

    public List<ProductDTO> clearTheCartDueToFinishedOrder() {
        List<ProductDTO> productDTOs = cart.getProductsInCartAsDTO();
        cart.clearTheCart();
        return productDTOs;
    }
}
