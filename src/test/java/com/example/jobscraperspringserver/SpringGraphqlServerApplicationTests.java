package com.example.jobscraperspringserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    properties = { "spring.data.mongodb.host=localhost" }
)
class SpringGraphqlServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
