package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.types.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service
public class ScrapesScheduler {

    @Autowired
    ReactiveMongoTemplate mongoTemplate;
    @Autowired
    ScrapeRequestsSender scrapeRequestsSender;
    @Autowired
    PagePublisher pagePublisher;
    private static String JWT_TOKEN;
    private static Date JWT_TOKEN_EXPIRATION;

    @Scheduled(fixedRate = 60000, initialDelay = 80000)
    public void checkScrapesToPerform() {
        mongoTemplate.findAll(Page.class).collectList().subscribe(pages -> {
            pages.stream().forEach(page -> {
                if (page.getLastScrapePerformed() == null || new Date().getTime() - page.getLastScrapePerformed().getTime() > page.getInterval()) {
                    scrapeRequestsSender.performScrapeRequest(JWT_TOKEN, page.getHost(), page.getPath(), page.getJobAnchorSelector(), page.getJobLinkContains(), page.getNumberOfPages(), page.getUserUuid());
                    page.setLastScrapePerformed(new Date());
                    mongoTemplate.save(page);
                    pagePublisher.publish(new Date().toString());
                }
            });
        });
    }

    @Scheduled(fixedRate = 60000, initialDelay = 20000)
    public void checkAuthorization() {
        if (JWT_TOKEN_EXPIRATION == null || is5minBefore(JWT_TOKEN_EXPIRATION)) {
            scrapeRequestsSender.loginWebflux().collectList().subscribe(tokenExp -> {
                if (tokenExp != null) {
                    JWT_TOKEN = (String) tokenExp.get(0);
                    JWT_TOKEN_EXPIRATION = (Date) tokenExp.get(1);
                }
            });
        }
    }

    private boolean is5minBefore(Date expiration) {
        long diff = expiration.getTime() - new Date().getTime();
        return diff < 5 * 60 * 1000;
    }
}
