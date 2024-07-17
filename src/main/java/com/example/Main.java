//package com.example;
//
//import com.example.lib.Injector;
//import com.example.model.Product;
//import com.example.service.ProductService;
//import java.math.BigDecimal;
//
//public class Main {
//    public static void main(String[] args) {
//        Product iphone = new Product("iPhone", BigDecimal.valueOf(1000));
//
//        Product samsung = new Product("Samsung", BigDecimal.valueOf(800));
//
//        Injector injector = Injector.getInjectorsInstance("com.example");
//        ProductService productService =
//                (ProductService) injector.getObjectsInstance(ProductService.class);
//
//        productService.save(iphone);
//        productService.save(samsung);
//
//        System.out.println(productService.findAll());
//    }
//}




package com.example;

import com.example.lib.Injector;
import com.example.model.Product;
import com.example.service.ProductService;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Product iphone = new Product("iPhone", BigDecimal.valueOf(1000));

        Product samsung = new Product("Samsung", BigDecimal.valueOf(800));

        ProductService productService =
                (ProductService) Injector.getInstance(ProductService.class);

        productService.save(iphone);
        productService.save(samsung);

        System.out.println(productService.findAll());
    }
}
