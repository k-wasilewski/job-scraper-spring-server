package com.example.springgraphqlserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.springgraphqlserver.services.ProductService;
import com.example.springgraphqlserver.types.Product;
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
