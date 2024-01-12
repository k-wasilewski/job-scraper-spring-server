package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.types.Page;
import com.mongodb.client.result.DeleteResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.jobscraperspringserver.repositories.PageMongoRepository;

import java.util.Comparator;
import java.util.NoSuchElementException;

@Service
public class PageService {
    @Autowired
    ReactiveMongoTemplate mongoTemplate;
    @Autowired
    PageMongoRepository pageMongoRepository;
    @Autowired
    UserService userService;

    public Flux<Page> getPages() {
        return userService.getCurrentUserUuid().flatMapMany(uuid -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("userUuid").is(uuid));
            return pageMongoRepository.findAllByUserUuid(uuid);
        });
    }

    public Mono<Page> deletePage(int id) {
        return userService.getCurrentUserUuid().flatMap(uuid -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("userUuid").is(uuid));
            query.addCriteria(Criteria.where("id").is(id));
            return mongoTemplate.findOne(query, Page.class).flatMap(toDelete -> {
                return mongoTemplate.remove(toDelete).flatMap(result -> {
                    if (result.getDeletedCount() == 1) return Mono.just(toDelete);
                    else return Mono.empty();
                });
            });
        });
    }

    public Mono<Page> modifyPage(int id, Page page) {
        return userService.getCurrentUserUuid().flatMap(uuid -> {
            page.setId(id);
            page.setUserUuid(uuid);

            return mongoTemplate.save(page);
        });
    }

    public Mono<Page> addPage(Page page) {
        return userService.getCurrentUserUuid().flatMap(uuid -> {
            return getHighestId().flatMap(highestId -> {
                page.setId(highestId + 1);
                page.setUserUuid(uuid);

                return mongoTemplate.insert(page);
            });
        });
    }

    private Mono<Integer> getHighestId() {
        try {
            return getAllPages().collectList().flatMap(pages -> {
                return Mono.just(pages.stream().max(Comparator.comparing(Page::getId)).get().getId());
            });
        } catch (NoSuchElementException e) {
            return Mono.just(0);
        }
    }

    private Flux<Page> getAllPages() {
        return mongoTemplate.findAll(Page.class);
    }
}
