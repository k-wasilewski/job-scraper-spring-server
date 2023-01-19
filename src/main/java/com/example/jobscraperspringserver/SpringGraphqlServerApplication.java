package com.example.jobscraperspringserver;

import com.example.jobscraperspringserver.services.ScrapeRequestsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import javax.annotation.PostConstruct;

//TODO: keep user's config in db and make requests to node-server in intervals, notify user when request was made to include it in the dashboard's healthcheck ("last scrape was performede at [timestamp]")
@SpringBootApplication
@EnableWebFlux
public class SpringGraphqlServerApplication {

    @Autowired
    ScrapeRequestsSender requestsSender;
    public static void main(String[] args) {
        SpringApplication.run(SpringGraphqlServerApplication.class, args);
    }

    @PostConstruct
    public void dsdsd() {
        System.out.println("-----------------------------siemanko-----------------------------------------");
        //requestsSender.loginWebflux();
    }
}
