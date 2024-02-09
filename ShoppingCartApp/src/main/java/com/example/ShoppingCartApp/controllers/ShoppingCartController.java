package com.example.ShoppingCartApp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.ShoppingCartApp.entities.CartItem;
import com.example.ShoppingCartApp.entities.User;
import com.example.ShoppingCartApp.services.ShoppingCartService;
import com.example.ShoppingCartApp.services.UserService;

@RestController
@RequestMapping("/shopping")
public class ShoppingCartController {

	private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @PostMapping("/addToCart/{userId}/{productId}/{quantity}")
    public ResponseEntity<Void> addToCart(@PathVariable Long userId, @PathVariable Long productId, @PathVariable int quantity) {
        try {
            User user = userService.getUserById(userId);
            shoppingCartService.addToCart(user, productId, quantity);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @DeleteMapping("/removeFromCart/{userId}/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId, @PathVariable Long cartItemId) {
    	try {
            User user = userService.getUserById(userId);
            shoppingCartService.removeFromCart(user, cartItemId);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @GetMapping("/getCart/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
    	try {
    		User user = userService.getUserById(userId);
            if (user != null) {
                List<CartItem> cartItems = shoppingCartService.getCartItems(user);
                return ResponseEntity.ok(cartItems);
            }
            return ResponseEntity.notFound().build();
		} catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @GetMapping("/getTotal/{userId}")
    public ResponseEntity<Double> getTotal(@PathVariable Long userId) {
    	User user = userService.getUserById(userId);
        if (user != null) {
            double total = shoppingCartService.getTotal(user);
            return ResponseEntity.ok(total);
        }
        return ResponseEntity.notFound().build();
    }
}
