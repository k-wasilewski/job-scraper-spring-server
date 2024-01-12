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
import com.example.jobscraperspringserver.repositories.UserMongoRepository;

@Service
public class UserService {
    @Autowired
    ReactiveMongoTemplate mongoTemplate;
    @Autowired UserMongoRepository userMongoRepository;

    public Mono<String> getCurrentUserUuid() {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(ctx -> {
                UserDetails principal = (UserDetails) ctx.getAuthentication().getPrincipal();
                return findUserByEmail(principal.getUsername()).flatMap(user -> Mono.just(user.getUuid()));
            });
    }

    public Mono<User> findUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return userMongoRepository.findFirstByEmail(email);
    }
}
