package com.example.ShoppingCartApp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ShoppingCartApp.entities.CartItem;
import com.example.ShoppingCartApp.entities.User;
import com.example.ShoppingCartApp.repositories.CartItemRepository;
import com.example.ShoppingCartApp.repositories.UserRepository;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{

	private final ProductService productService;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public ShoppingCartServiceImpl(ProductService productService, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void addToCart(User user, Long productId, int quantity) {
        Optional<User> existingUser = userRepository.findById(user.getId());

        if (existingUser.isPresent()) {
            // User exists, add to their cart
            CartItem cartItem = new CartItem(existingUser.get(), productService.getProductById(productId), quantity);
            existingUser.get().getCartItems().add(cartItem);
            userRepository.save(existingUser.get());
        } else {
            // New user, create a cart for them
            User newUser = new User();
            newUser.setId(user.getId());
            CartItem cartItem = new CartItem(newUser, productService.getProductById(productId), quantity);
            newUser.setCartItems(List.of(cartItem));
            userRepository.save(newUser);
        }
    }

    @Override
    public void removeFromCart(User user, Long cartItemId) {
    	Optional<User> existingUserOptional = userRepository.findById(user.getId());

        existingUserOptional.ifPresent(existingUser -> {
            List<CartItem> cartItems = existingUser.getCartItems();
            cartItems.removeIf(item -> item.getId().equals(cartItemId));
            existingUser.setCartItems(cartItems);

            userRepository.save(existingUser);

            cartItemRepository.deleteById(cartItemId);
        });
    }

    @Override
    public List<CartItem> getCartItems(User user) {
        return userRepository.findById(user.getId())
                .map(User::getCartItems)
                .orElse(List.of());
    }

    @Override
    public double getTotal(User user) {
        return userRepository.findById(user.getId())
                .map(u -> u.getCartItems().stream()
                        .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                        .sum())
                .orElse(0.0);
    }

}
