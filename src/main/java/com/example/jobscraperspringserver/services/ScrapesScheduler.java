package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.types.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScrapesScheduler {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ScrapeRequestsSender scrapeRequestsSender;
    @Autowired
    PagePublisher pagePublisher;

    @Scheduled(fixedRate = 60000)
    public void checkScrapesToPerform() {
        List<Page> pages = mongoTemplate.findAll(Page.class);
        pages.stream().forEach(page -> {
            if (page.getLastScrapePerformed() == null || new Date().getTime() - page.getLastScrapePerformed().getTime() > page.getInterval()) {
                scrapeRequestsSender.performScrapeRequest(page.getHost(), page.getPath(), page.getJobAnchorSelector(), page.getJobLinkContains(), page.getNumberOfPages());
                page.setLastScrapePerformed(new Date());
                mongoTemplate.save(page);
                pagePublisher.publish(new Date().toString());
            }
        });
    }
}
