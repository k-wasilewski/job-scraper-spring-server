package com.example.jobscraperspringserver.services;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;

import com.github.tomakehurst.wiremock.WireMockServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.springframework.beans.factory.annotation.Autowired;

import reactor.test.StepVerifier;

import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    properties = { "spring.data.mongodb.host=localhost" }
)
public class ScrapeRequestsSenderTest {

    private ScrapeRequestsSender scrapeRequestsSender;
    @Autowired
    private PageService pageService;

    public WireMockServer wireMockServer;
    WebClient mockWebClient;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        mockWebClient = WebClient.create(wireMockServer.baseUrl()+"/graphql");
        scrapeRequestsSender = new ScrapeRequestsSender(mockWebClient, pageService);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void loginWebfluxTest() {
        // GIVEN
        String authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiMDdiMmEzMjYtYzA5ZS00MTQxLWIyMTctZjdiYjY0MWY2MmIzIiwiZW1haWwiOiJhYmNAYWJjLnBsIiwiaWF0IjoxNzA2NTI4NzM4LCJleHAiOjE3MDY1MzU5Mzh9.WFun_los8MoWSV8V-GKX286Ozng4tNh2mevjvveJDwE";
        Date date = new Date(2024-1900, 1, 29, 15, 0, 0);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzzz");
        String dateFormatted = formatter.format(date);

        createNodeServerLoginMockStub(dateFormatted, authToken);

        // WHEN
        Flux<Object> tokenFlux = scrapeRequestsSender.loginWebflux();

        // THEN
        StepVerifier.create(tokenFlux)
            .expectNext(authToken)
            .assertNext(d -> {
                Date respDate = (Date) d;
                assertEquals(respDate.getTime(), date.getTime());
            })
            .expectComplete()
            .verify();
    }

    @Test
    public void performScrapeRequestTest() {
        // GIVEN
        createNodeServerScrapeMockStub();
        String jsonInputString = "{ \"query\": \"mutation { scrape(host: \\\"" + "host" + "\\\", path: \\\"" + "path" + "\\\", jobAnchorSelector: \\\"" + "jobAnchorSelector" + "\\\", jobLinkContains: \\\"" + "jobLinkContains" + "\\\", numberOfPages: " + 1 + ", userUuid: \\\"" + "userUuid" + "\\\") { complete } }\" }";

        // WHEN
        Date resp = scrapeRequestsSender.performScrapeRequest("myToken", "host", "path", "jobAnchorSelector", "jobLinkContains", 1, "userUuid");

        // THEN
        assertNotNull(resp);

        wireMockServer.verify(postRequestedFor(urlEqualTo("/graphql"))
            .withHeader("Origin", containing("job-scraper-spring-server:8081"))
            .withHeader("Cookie", containing("authToken=myToken;"))
            .withRequestBody(containing(jsonInputString)));      
    }

    private void createNodeServerLoginMockStub(String dateFormatted, String token) {
        wireMockServer.stubFor(post(urlEqualTo("/graphql"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Set-Cookie", "authToken="+token+";Expires="+dateFormatted)
                    .withBody("okej"))
            );
    }

    private void createNodeServerScrapeMockStub() {
        wireMockServer.stubFor(post(urlEqualTo("/graphql"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withBody("{ complete: true }"))
            );
    }
}