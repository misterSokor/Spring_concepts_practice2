package com.example.service.impl;

import com.example.dao.ProductDao;
import com.example.lib.InjectConstructor;
import com.example.lib.Service;
import com.example.model.Product;
import com.example.service.ProductService;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;
    @InjectConstructor
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
