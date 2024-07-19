
package com.example;

import com.example.config.AppConfig;
import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        /* Steps to implement the Spring without HibernateUtil class,
        sessionFactory
        1. Add spring-orm and commons-dbcp2 dependencies to the pom.xml file.
        2. Create DataSource and LocalSessionFactoryBean beans in AppConfig
        class
        3. Mark them with @Bean annotation
         */

        Product iphone = new Product("iPhone", BigDecimal.valueOf(1000));

        Product samsung = new Product("Samsung", BigDecimal.valueOf(800));
        Product xiaomi = new Product("Xiaomi", BigDecimal.valueOf(500));

//        ProductService productService =
//                (ProductService) Injector.getInstance(ProductService.class);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        ProductService productService = context.getBean(ProductService.class);

        productService.save(iphone);
        productService.save(samsung);
        productService.save(xiaomi);

        System.out.println(productService.findAll());
    }
}
