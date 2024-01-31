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
import reactor.core.publisher.Mono;
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

    @Test
    void addPageTest() {
        // GIVEN
        Mono<Page> page = Mono.just(createTestPage());
        String jsonInputString = "{ \"query\": \"mutation { addPage(host: \\\"" + "host" + "\\\", path: \\\"" + "path" + "\\\", jobAnchorSelector: \\\"" +"jobAnchorSelector" + "\\\", jobLinkContains: \\\"" + "jobLinkContains" + "\\\", numberOfPages: " + 1 + ", interval: " + 1 + ") { id, host, path, jobAnchorSelector, jobLinkContains, numberOfPages, interval } }\" }";
        when(pageService.addPage(any(Page.class))).thenReturn(page);

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
            .jsonPath("$.data.addPage.id").isEqualTo(0)
            .jsonPath("$.data.addPage.host").isEqualTo("host")
            .jsonPath("$.data.addPage.path").isEqualTo("path")
            .jsonPath("$.data.addPage.jobAnchorSelector").isEqualTo("jobAnchorSelector")
            .jsonPath("$.data.addPage.jobLinkContains").isEqualTo("jobLinkContains")
            .jsonPath("$.data.addPage.numberOfPages").isEqualTo(1)
            .jsonPath("$.data.addPage.interval").isEqualTo(1);
    }

    @Test
    void deletePageTest() {
        // GIVEN
        Mono<Page> page = Mono.just(createTestPage());
        String jsonInputString = "{ \"query\": \"mutation { deletePage(id: " + 0 + ") { id, host, path, jobAnchorSelector, jobLinkContains, numberOfPages, interval } }\" }";
        when(pageService.deletePage(0)).thenReturn(page);

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
            .jsonPath("$.data.deletePage.id").isEqualTo(0)
            .jsonPath("$.data.deletePage.host").isEqualTo("host")
            .jsonPath("$.data.deletePage.path").isEqualTo("path")
            .jsonPath("$.data.deletePage.jobAnchorSelector").isEqualTo("jobAnchorSelector")
            .jsonPath("$.data.deletePage.jobLinkContains").isEqualTo("jobLinkContains")
            .jsonPath("$.data.deletePage.numberOfPages").isEqualTo(1)
            .jsonPath("$.data.deletePage.interval").isEqualTo(1);
    }

    @Test
    void modifyPageTest() {
        // GIVEN
        Mono<Page> page = Mono.just(createTestPage());
        String jsonInputString = "{ \"query\": \"mutation { modifyPage(id: 0, host: \\\"" + "host" + "\\\", path: \\\"" + "path" + "\\\", jobAnchorSelector: \\\"" +"jobAnchorSelector" + "\\\", jobLinkContains: \\\"" + "jobLinkContains" + "\\\", numberOfPages: " + 1 + ", interval: " + 1 + ") { id, host, path, jobAnchorSelector, jobLinkContains, numberOfPages, interval } }\" }";
        when(pageService.modifyPage(eq(0), any(Page.class))).thenReturn(page);

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
            .jsonPath("$.data.modifyPage.id").isEqualTo(0)
            .jsonPath("$.data.modifyPage.host").isEqualTo("host")
            .jsonPath("$.data.modifyPage.path").isEqualTo("path")
            .jsonPath("$.data.modifyPage.jobAnchorSelector").isEqualTo("jobAnchorSelector")
            .jsonPath("$.data.modifyPage.jobLinkContains").isEqualTo("jobLinkContains")
            .jsonPath("$.data.modifyPage.numberOfPages").isEqualTo(1)
            .jsonPath("$.data.modifyPage.interval").isEqualTo(1);
    }

    private Page createTestPage() {
        Page page = new Page();
        page.setHost("host");
        page.setPath("path");
        page.setJobAnchorSelector("jobAnchorSelector");
        page.setJobLinkContains("jobLinkContains");
        page.setNumberOfPages(1);
        page.setInterval(1);
        return page;
    }
}