package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    public String getCurrentUserUuid() {
        UserDetails principal = (UserDetails) ReactiveSecurityContextHolder.getContext().block().getAuthentication().getPrincipal();
        String uuid = findUserByEmail(principal.getUsername()).block().getUuid();
        return uuid;
    }

    public Mono<User> findUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.find(query, User.class).take(1).single();
    }
}
