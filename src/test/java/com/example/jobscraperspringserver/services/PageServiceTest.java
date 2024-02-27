package com.example.jobscraperspringserver.services;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import org.mockito.Mock;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.jobscraperspringserver.repositories.PageMongoRepository;

import com.example.jobscraperspringserver.types.Page;

import static org.junit.jupiter.api.Assertions.*;

import reactor.test.StepVerifier;

import java.util.List;

import com.mongodb.client.result.DeleteResult;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    properties = { "spring.data.mongodb.host=localhost" }
)
public class PageServiceTest {

    @Autowired
    private PageService pageService;

    @MockBean
    private UserService userService;
    
    @MockBean
    private PageMongoRepository pageMongoRepository;

    @SpyBean
    private ReactiveMongoTemplate mongoTemplate;

    @Mock
    private DeleteResult deleteResult;

    @Captor
    ArgumentCaptor<Page> pageCaptor;

    private List<Page> mockPages = List.of(new Page(1), new Page(2));
    private String mockUuid = "d0fa48ab-bee6-4b08-8077-cab7c92bbec3";
    private int mockId = 12;

    @Test
    public void getPagesTest() {
        // GIVEN
        when(userService.getCurrentUserUuid()).thenReturn(Mono.just(mockUuid));
        when(pageMongoRepository.findAllByUserUuid(mockUuid)).thenReturn(Flux.fromIterable(mockPages));

        // WHEN
        Flux<Page> returnedPages = pageService.getPages();

        // THEN
        StepVerifier.create(returnedPages)
            .expectNext(mockPages.get(0))
            .expectNext(mockPages.get(1))
            .expectComplete()
            .verify();
    }

    @Test
    public void deletePagePresentTest() {
        // GIVEN
        when(userService.getCurrentUserUuid()).thenReturn(Mono.just(mockUuid));
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(mockUuid));
        query.addCriteria(Criteria.where("id").is(mockId));
        when(mongoTemplate.findOne(query, Page.class)).thenReturn(Mono.just(mockPages.get(0)));
        when(deleteResult.getDeletedCount()).thenReturn(1L);
        when(mongoTemplate.remove(mockPages.get(0))).thenReturn(Mono.just(deleteResult));

        // WHEN
        Mono<Page> res = pageService.deletePage(mockId);

        // THEN
        StepVerifier.create(res)
            .expectNext(mockPages.get(0))
            .expectComplete()
            .verify();
    }

    @Test
    public void deletePageEmptyTest() {
        // GIVEN
        when(userService.getCurrentUserUuid()).thenReturn(Mono.just(mockUuid));
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(mockUuid));
        query.addCriteria(Criteria.where("id").is(mockId));
        when(mongoTemplate.findOne(query, Page.class)).thenReturn(Mono.just(mockPages.get(0)));
        when(deleteResult.getDeletedCount()).thenReturn(0L);
        when(mongoTemplate.remove(mockPages.get(0))).thenReturn(Mono.empty());

        // WHEN
        Mono<Page> res = pageService.deletePage(mockId);

        // THEN
        StepVerifier.create(res)
            .expectNextCount(0L)
            .expectComplete()
            .verify();
    }

    @Test
    public void modifyPageTest() {
        // GIVEN
        when(userService.getCurrentUserUuid()).thenReturn(Mono.just(mockUuid));
        //when(mongoTemplate.save(any(Page.class)).thenReturn(mockPages.get(0)));
        doReturn(Mono.just(mockPages.get(0))).when(mongoTemplate).save(any(Page.class));

        // WHEN
        Mono<Page> res = pageService.modifyPage(mockId, mockPages.get(0));

        // THEN
        StepVerifier.create(res)
            .expectNextCount(1L)
            .expectComplete()
            .verify();

        verify(mongoTemplate).save(pageCaptor.capture());
        Page capturedPage = pageCaptor.getValue();

        assertEquals(capturedPage.getId(), mockId);
        assertEquals(capturedPage.getUserUuid(), mockUuid);
    }

    @Test
    public void addPagePresentTest() {
        // GIVEN
        when(userService.getCurrentUserUuid()).thenReturn(Mono.just(mockUuid));
        when(mongoTemplate.findAll(Page.class)).thenReturn(Flux.fromIterable(mockPages));
        //when(mongoTemplate.save(any(Page.class)).thenReturn(mockPages.get(0)));
        doReturn(Mono.just(mockPages.get(0))).when(mongoTemplate).insert(any(Page.class));

        // WHEN
        Mono<Page> res = pageService.addPage(mockPages.get(0));

        // THEN
        StepVerifier.create(res)
            .expectNextCount(1L)
            .expectComplete()
            .verify();

        verify(mongoTemplate).insert(pageCaptor.capture());
        Page capturedPage = pageCaptor.getValue();

        assertEquals(capturedPage.getUserUuid(), mockUuid);
        assertEquals(capturedPage.getId(), mockPages.size() + 1);
    }

    @Test
    public void addPageEmptyTest() {
        // GIVEN
        when(userService.getCurrentUserUuid()).thenReturn(Mono.just(mockUuid));
        when(mongoTemplate.findAll(Page.class)).thenReturn(Flux.empty());
        //when(mongoTemplate.save(any(Page.class)).thenReturn(mockPages.get(0)));
        doReturn(Mono.just(mockPages.get(0))).when(mongoTemplate).insert(any(Page.class));

        // WHEN
        Mono<Page> res = pageService.addPage(mockPages.get(0));

        // THEN
        StepVerifier.create(res)
            .expectNextCount(1L)
            .expectComplete()
            .verify();
            
        verify(mongoTemplate).insert(pageCaptor.capture());
        Page capturedPage = pageCaptor.getValue();

        assertEquals(capturedPage.getUserUuid(), mockUuid);
        assertEquals(capturedPage.getId(), 1);
    }
}