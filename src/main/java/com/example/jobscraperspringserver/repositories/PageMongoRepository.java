package com.example.jobscraperspringserver.repositories;

import com.example.jobscraperspringserver.types.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PageMongoRepository extends MongoRepository <Page, Integer> {

}