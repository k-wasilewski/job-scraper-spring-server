package com.example.jobscraperspringserver.repositories;

import com.example.jobscraperspringserver.types.Page;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PageMongoRepository extends ReactiveMongoRepository<Page, Integer> {
    Flux<Page> findAllByUserUuid(String userUuid);
}