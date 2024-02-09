package com.example.ShoppingCartApp.services;

import java.util.List;

import com.example.ShoppingCartApp.entities.CartItem;
import com.example.ShoppingCartApp.entities.User;

public interface ShoppingCartService {
	
	void addToCart(User user, Long productId, int quantity);

    void removeFromCart(User user, Long productId);

    List<CartItem> getCartItems(User user);

    double getTotal(User user);
}
