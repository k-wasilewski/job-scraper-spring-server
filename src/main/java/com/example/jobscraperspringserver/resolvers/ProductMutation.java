package com.example.jobscraperspringserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.jobscraperspringserver.services.ProductService;
import com.example.jobscraperspringserver.types.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMutation implements GraphQLMutationResolver {
    @Autowired
    ProductService productService;

    public Product addProduct(String content) { return productService.addProduct(content); }

    public Product deleteProduct(int id) {
        return productService.deleteProduct(id);
    }
}
