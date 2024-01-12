package com.example.jobscraperspringserver.repositories;

import com.example.jobscraperspringserver.types.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserMongoRepository extends ReactiveMongoRepository<User, Integer> {
    Mono<User> findFirstByEmail(String email);
}