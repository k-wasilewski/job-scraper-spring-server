package com.example.jobscraperspringserver.resolvers;

import com.example.jobscraperspringserver.resolvers.PageController;
import com.example.jobscraperspringserver.services.PageService;
import com.example.jobscraperspringserver.types.Page;

import org.springframework.http.MediaType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    properties = { "spring.data.mongodb.host=localhost" }
)
@AutoConfigureWebTestClient
@WithMockUser(username="test@test.pl", password="pwd", roles="user")
public class PageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PageService pageService;

    @Test
    void getPagesTest() {
        // GIVEN
        Flux<Page> pages = Flux.just(new Page(1), new Page(2));
        String jsonInputString = "{ \"query\": \"{ getPages { id, host, path, jobAnchorSelector, jobLinkContains, numberOfPages, interval } }\" }";
        when(pageService.getPages()).thenReturn(pages);

        // WHEN, THEN
        webTestClient
            .post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(jsonInputString)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody()
            .jsonPath("$.data.getPages").isArray()
            .jsonPath("$.data.getPages.length()").isEqualTo(2)
            .jsonPath("$.data.getPages[0].id").isEqualTo(1)
            .jsonPath("$.data.getPages[1].id").isEqualTo(2);
    }

}