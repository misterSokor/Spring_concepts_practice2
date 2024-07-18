
package com.example;

import com.example.config.AppConfig;
import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        /* Steps to implement the Spring Framework:
        1. Add Spring-context maven dependency to the pom.xml file.
        2. Create AppConfig class and mark it with the @Configuration annotation.
        3. Replace @Dao and @Service annotations with the @Autowired in ProductServiceImpl .
        4. Replace @Inject annotation with the @Autowired in the ProductServiceImpl
        5. Replace creating of Injector instance with
        AnnotationConfigApplicationContext
         */

        Product iphone = new Product("iPhone", BigDecimal.valueOf(1000));

        Product samsung = new Product("Samsung", BigDecimal.valueOf(800));

//        ProductService productService =
//                (ProductService) Injector.getInstance(ProductService.class);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        ProductService productService = context.getBean(ProductService.class);

        productService.save(iphone);
        productService.save(samsung);

        System.out.println(productService.findAll());
    }
}
