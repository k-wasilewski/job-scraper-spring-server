package com.example.jobscraperspringserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.jobscraperspringserver.services.ProductService;
import com.example.jobscraperspringserver.types.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductQuery implements GraphQLQueryResolver {
    @Autowired
    ProductService productService;

    public List<Product> getAllProducts() {
        return productService.getProductsList();
    }

    public Product getProduct(int id) {
        return productService.getProduct(id);
    }
}
