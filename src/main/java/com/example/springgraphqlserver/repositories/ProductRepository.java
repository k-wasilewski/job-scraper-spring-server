package com.example.springgraphqlserver.repositories;

import com.example.springgraphqlserver.types.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository <Product, Integer> {

}