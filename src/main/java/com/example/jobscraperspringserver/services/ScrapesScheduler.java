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
    public void checkScrapesToPerform() {//TODO: remove mock pages, uncomment read&modify pages at mongodb
        List<Page> pages = new ArrayList<>();
        Page p1 = new Page();
        p1.setHost("https://myhost.com");
        p1.setPath("/jobs/reactnspring");
        p1.setJobLinkContains("");
        p1.setJobAnchorSelector("a");
        p1.setNumberOfPages(1);
        p1.setId(1);
        p1.setInterval(2000);
        pages.add(p1);
        Page p2 = new Page();
        p2.setHost("https://otherhost.pl");
        p2.setPath("/get_jobs/react/spring/get");
        p2.setJobLinkContains("");
        p2.setJobAnchorSelector("li[class*=\"job\"]");
        p2.setNumberOfPages(1);
        p2.setId(2);
        p2.setInterval(1000);
        pages.add(p2);
        //List<Page> pages = mongoTemplate.findAll(Page.class);
        pages.stream().forEach(page -> {
            if (page.getLastScrapePerformed() == null || new Date().getTime() - page.getLastScrapePerformed().getTime() > page.getInterval()) {
                scrapeRequestsSender.performScrapeRequest(page.getHost(), page.getPath(), page.getJobAnchorSelector(), page.getJobLinkContains(), page.getNumberOfPages());
                page.setLastScrapePerformed(new Date());
                //mongoTemplate.save(page);
                pagePublisher.publish(new Date().toString());
            }
        });
    }
}
