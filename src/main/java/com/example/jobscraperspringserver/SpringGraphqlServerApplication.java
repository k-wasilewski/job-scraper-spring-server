package com.example.jobscraperspringserver;

import com.example.jobscraperspringserver.services.ScrapeRequestsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableWebFlux
public class SpringGraphqlServerApplication {

    @Autowired
    ScrapeRequestsSender requestsSender;
    public static void main(String[] args) {
        SpringApplication.run(SpringGraphqlServerApplication.class, args);
    }
}
