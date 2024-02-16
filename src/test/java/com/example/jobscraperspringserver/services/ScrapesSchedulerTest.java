package com.example.jobscraperspringserver.services;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.lang.*;

import com.example.jobscraperspringserver.types.Page;

import static org.mockito.Mockito.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    properties = { "spring.data.mongodb.host=localhost" }
)
public class ScrapesSchedulerTest {

    @SpyBean
    ReactiveMongoTemplate mongoTemplate;
    @MockBean
    ScrapeRequestsSender scrapeRequestsSender;
    @MockBean
    PagePublisher pagePublisher;

    @Autowired
    ScrapesScheduler scrapesScheduler;

    @Captor
    ArgumentCaptor<Page> pageCaptor;

    @Test
    public void checkScrapesToPerformTest() {
        // GIVEN
        Date datePublished = new Date();
        Page p1 = new Page(1);
        p1.setHost("host");
        p1.setPath("path");
        p1.setJobAnchorSelector("jobAnchorSelector");
        p1.setJobLinkContains("jobLinkContains");
        p1.setUserUuid("userUuid");
        Date p1Date = new Date(10, 1, 1);
        p1.setLastScrapePerformed(p1Date);
        p1.setInterval(1);
        Page p2 = new Page(2);
        p2.setLastScrapePerformed(new Date(1000, 1, 1));
        p2.setInterval(1);
        Flux<Page> pages = Flux.just(p1, p2);
        when(mongoTemplate.findAll(Page.class)).thenReturn(pages);
        when(scrapeRequestsSender.performScrapeRequest(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(Integer.class), any(String.class))).thenReturn(datePublished);
        //when(mongoTemplate.save(new Page())).thenReturn(Mono.just(new Page(1)));

        // WHEN
        scrapesScheduler.checkScrapesToPerform();

        // THEN
        verify(scrapeRequestsSender, times(1)).performScrapeRequest(null, "host", "path", "jobAnchorSelector", "jobLinkContains", 0, "userUuid");
        verify(pagePublisher, times(1)).publish(any(String.class));
        verify(mongoTemplate).save(pageCaptor.capture());
        Page capturedPage = pageCaptor.getValue();

        assertTrue(capturedPage.getId() == p1.getId());

        assertTrue(capturedPage.getLastScrapePerformed().after(p1Date));
    }
}