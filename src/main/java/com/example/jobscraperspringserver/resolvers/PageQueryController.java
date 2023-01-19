package com.example.jobscraperspringserver.resolvers;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.reactivestreams.Publisher;
import com.example.jobscraperspringserver.types.Page;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.springframework.web.bind.annotation.GetMapping;

@Controller//webflux doesn't recognize this controller!
public class PageQueryController {
    @QueryMapping("getPages")
    public Publisher<Page> getPages() {
        System.out.println("getPages in controller");
        return Flux.just(new Page(2));
    }

    @GetMapping("/abc")
    public Publisher<String> sdsd() {
        return Mono.just("siemanko");
    }
}
