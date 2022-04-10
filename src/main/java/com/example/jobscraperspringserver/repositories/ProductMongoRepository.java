package com.example.jobscraperspringserver.repositories;

import com.example.jobscraperspringserver.types.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductMongoRepository extends MongoRepository <Product, Integer> {

}