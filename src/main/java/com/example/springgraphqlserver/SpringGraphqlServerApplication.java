package com.example.springgraphqlserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//TODO: keep user's config in db and make requests to node-server in intervals, notify user when request was made to include it in the dashboard's healthcheck ("last scrape was performede at [timestamp]")
@SpringBootApplication
public class SpringGraphqlServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringGraphqlServerApplication.class, args);
    }
}
