package com.example.jobscraperspringserver.repositories;

import com.example.jobscraperspringserver.types.Page;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PageMongoRepository extends ReactiveMongoRepository<Page, Integer> {

}