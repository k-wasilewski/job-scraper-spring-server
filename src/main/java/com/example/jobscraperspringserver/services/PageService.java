package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.types.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PageService {
    @Autowired
    ReactiveMongoTemplate mongoTemplate;
    @Autowired
    UserService userService;

    public Flux<Page> getPages() {
        System.out.println("getPages in service");
        //String uuid = userService.getCurrentUserUuid();
        //Query query = new Query();
        //query.addCriteria(Criteria.where("userUuid").is(uuid));
        //return mongoTemplate.find(query, Page.class);
        //Flux<Page> pages = mongoTemplate.findAll(Page.class).doOnError(error -> System.out.println(error)).onErrorReturn(new Page(666));
        //pages.subscribe(System.out::println);
        return Flux.just(new Page(2)).doOnNext(page -> {
            System.out.println("sdfdfd");
        });
    }

    public Mono<Page> deletePage(int id) {
        String uuid = userService.getCurrentUserUuid();
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(uuid));
        Mono<Page> toDelete;

        return mongoTemplate.find(query, Page.class).take(1).single();
    }

    public Mono<Page> modifyPage(int id, Page page) {
        String uuid = userService.getCurrentUserUuid();
        page.setId(id);
        page.setUserUuid(uuid);

        return mongoTemplate.save(page);
    }

    public Mono<Page> addPage(Page page) {
        String uuid = userService.getCurrentUserUuid();
        page.setId(getHighestId() + 1);
        page.setUserUuid(uuid);

        return mongoTemplate.insert(page);
    }

    private int getHighestId() {
        try {
            return getAllPages().collectList().block().stream().max(Comparator.comparing(Page::getId)).get().getId();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    private Flux<Page> getAllPages() {
        return mongoTemplate.findAll(Page.class);
    }
}
