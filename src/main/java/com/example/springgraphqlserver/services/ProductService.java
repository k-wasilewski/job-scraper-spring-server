package com.example.springgraphqlserver.services;

import com.example.springgraphqlserver.types.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {
    private List<Product> productsList;

    public ProductService() {
        this.productsList = new ArrayList(Arrays.asList(new Product(1, "Book"), new Product(2, "Cup"),
                new Product(3, "Plate"), new Product(4, "Phone"), new Product(5, "Flute")));
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public Product getProduct(int id) {
        return this.productsList.stream().filter(product -> product.getId() == id).findFirst()
                .orElseGet(null);
    }

    public Product addProduct(String content) {
        if (this.productsList.add(new Product(this.getHighestId() + 1, content)))
            return new Product(this.getHighestId() + 1, content);

        return null;
    }

    public Product deleteProduct(int id) {
        Product toDelete = this.productsList.stream().filter(p -> p.getId() == id).findFirst().get();
        if (this.productsList.remove(toDelete))
            return toDelete;

        return null;
    }

    private int getHighestId() {
        return this.productsList.stream().max(Comparator.comparing(Product::getId)).get().getId();
    }
}
