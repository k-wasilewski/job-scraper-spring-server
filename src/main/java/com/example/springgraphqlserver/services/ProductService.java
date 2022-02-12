package com.example.springgraphqlserver.services;

import com.example.springgraphqlserver.types.Product;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {
    private List<Product> productsList;

    public ProductService() {
        this.productsList = List.of(new Product(1, "Book"), new Product(2, "Cup"),
                new Product(3, "Plate"), new Product(4, "Phone"), new Product(5, "Flute"));
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public Product getProduct(int id) {
        return this.productsList.stream().filter(product -> product.getId() == id).findFirst()
                .orElseGet(null);
    }

    public boolean addProduct(String content) {
        return this.productsList.add(new Product(this.getHighestId() + 1, content));
    }

    private int getHighestId() {
        return this.productsList.stream().max(Comparator.comparing(Product::getId)).get().getId();
    }
}
