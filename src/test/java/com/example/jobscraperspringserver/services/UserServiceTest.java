package com.example.jobscraperspringserver.services;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.mockito.Mockito.*;
import org.mockito.Mock;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.jobscraperspringserver.repositories.UserMongoRepository;

import com.example.jobscraperspringserver.types.User;

import reactor.test.StepVerifier;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    properties = { "spring.data.mongodb.host=localhost" }
)
@WithMockUser(username="myemail@domain.com", password="pwd", roles="user")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    
    @MockBean
    private UserMongoRepository userMongoRepository;

    private static String mockEmail = "myemail@domain.com";
    private static String mockUuid = "1234-5678-90123456-12345678";
    private static User mockUser = new User(mockEmail);

    @BeforeAll
    public static void init() {
        mockUser.setUuid(mockUuid);
    }

    @Test
    public void getCurrentUserUuidTest() {
        // GIVEN
        when(userMongoRepository.findFirstByEmail(mockEmail)).thenReturn(Mono.just(mockUser));

        // WHEN
        Mono<String> returnedUuid = userService.getCurrentUserUuid();

        // THEN
        StepVerifier.create(returnedUuid)
            .expectNext(mockUuid)
            .expectComplete()
            .verify();

    }

    @Test
    public void findUserByEmailTest() {
        // GIVEN
        when(userMongoRepository.findFirstByEmail(mockEmail)).thenReturn(Mono.just(mockUser));

        // WHEN
        Mono<User> returnedUser = userService.findUserByEmail(mockEmail);

        // THEN
        StepVerifier.create(returnedUser)
            .expectNext(mockUser)
            .expectComplete()
            .verify();
    }
}