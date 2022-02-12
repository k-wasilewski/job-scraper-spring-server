package com.example.springgraphqlserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.springgraphqlserver.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMutation implements GraphQLQueryResolver {
    @Autowired
    ProductService productService;

    public boolean addProduct(String content) {
        return productService.addProduct(content);
    }
}
