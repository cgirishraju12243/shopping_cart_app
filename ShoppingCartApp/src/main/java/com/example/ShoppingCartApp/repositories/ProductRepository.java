package com.example.ShoppingCartApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ShoppingCartApp.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
