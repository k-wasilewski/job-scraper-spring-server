package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.repositories.ProductMongoRepository;
import com.example.jobscraperspringserver.repositories.ProductRepository;
import com.example.jobscraperspringserver.types.Product;
import com.example.jobscraperspringserver.types.ProductOperation;
import com.example.jobscraperspringserver.types.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {
    private List<Product> productsList;

    @Autowired
    ProductPublisher productPublisher;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMongoRepository productMongoRepository;

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
        Product toAdd = new Product(this.getHighestId() + 1, content);
        if (this.productsList.add(toAdd)) {
            productPublisher.publish("Added " + toAdd);
            productRepository.save(new ProductWrapper(toAdd, ProductOperation.ADD));
            mongoTemplate.insert(toAdd.withOperation(ProductOperation.ADD));
            productMongoRepository.save(toAdd.withOperation(ProductOperation.ADD));
            return toAdd;
        }
        return null;
    }

    public Product deleteProduct(int id) {
        Product toDelete = this.productsList.stream().filter(p -> p.getId() == id).findFirst().get();
        if (this.productsList.remove(toDelete)) {
            productPublisher.publish("Deleted " + toDelete);
            productRepository.save(new ProductWrapper(toDelete, ProductOperation.DELETE));
            mongoTemplate.insert(toDelete.withOperation(ProductOperation.DELETE));
            productMongoRepository.save(toDelete.withOperation(ProductOperation.DELETE));
            return toDelete;
        }

        return null;
    }

    private int getHighestId() {
        return this.productsList.stream().max(Comparator.comparing(Product::getId)).get().getId();
    }
}
