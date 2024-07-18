package com.example.service.impl;

import com.example.dao.ProductDao;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private  ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product save(Product product) {
        return productDao.save(product);
    }

    @Override
    public Product get(Long id) {
        return productDao.get(id);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }
}
