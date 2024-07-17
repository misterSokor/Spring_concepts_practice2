package com.example.service;

import com.example.model.Product;
import java.util.List;

public interface ProductService {
    Product save(Product product);

    Product get(Long id);

    List<Product> findAll();
}
