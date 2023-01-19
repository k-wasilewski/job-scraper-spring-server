package com.example.jobscraperspringserver.resolvers;

/*import com.coxautodev.graphql.tools.GraphQLQueryResolver;
//import graphql.kickstart.tools.GraphQLQueryResolver;
import com.example.jobscraperspringserver.services.PageService;
import com.example.jobscraperspringserver.types.Page;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.concurrent.CompletableFuture;

import java.util.List;

@Component
public class PageQuery implements GraphQLQueryResolver {
    @Autowired
    PageService pageService;

    //public CompletableFuture<List<Page>> getPages() {
    public Publisher<Page> getPages() {
        System.out.println("getPages in resolver");
        //return pageService.getPages();
        return Flux.just(new Page(2));//.collectList().toFuture();
    }
    /*
     *  type Query {
     *   getPages: Page
     *  }
     */
//}