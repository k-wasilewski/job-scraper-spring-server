package com.example.jobscraperspringserver.security;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.jobscraperspringserver.services.UserService;
import com.example.jobscraperspringserver.types.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import org.springframework.http.server.reactive.ServerHttpRequest;

import static org.mockito.Mockito.*;

import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

import org.springframework.http.HttpCookie;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import reactor.test.StepVerifier;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    properties = { "spring.data.mongodb.host=localhost" }
)
public class JwtReactiveRequestFilterTest {
    @MockBean
    private UserDetails userDetails;
    @MockBean
    private ServerHttpRequest serverHttpRequest;
    @MockBean
    private ServerWebExchange serverWebExchange;
    @MockBean
    private WebFilterChain webFilterChain;
    @MockBean
    private ReactiveUserDetailsService jwtUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserService userService;
    @Autowired
    private JwtReactiveRequestFilter jwtReactiveRequestFilter;

    private static final String token = "my-5ecret-+oken";
    private static MultiValueMap<String, HttpCookie> ckieMap = new LinkedMultiValueMap<String, HttpCookie>();
    private static final String email = "test@test.com";
    private static final String uuid = "e7350741-4ca4-4154-8439-adff5a6e31d6";
    private static User user = new User(email);
    private static final Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(List.of(new SimpleGrantedAuthority("user")));

    @BeforeAll
    public static void init() {
        user.setUuid(uuid);
        ckieMap.put("authToken", List.of(new HttpCookie("authToken", token)));
    }

    @Test
    public void happyPath_filterTest() {
        // GIVEN
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverHttpRequest.getCookies()).thenReturn(ckieMap);
        when(jwtTokenUtil.getEmailFromToken(token)).thenReturn(email);
        when(jwtTokenUtil.getUuidFromToken(token)).thenReturn(uuid);
        when(userService.findUserByEmail(email)).thenReturn(Mono.just(user));
        when(jwtUserDetailsService.findByUsername(email)).thenReturn(Mono.just(userDetails));
        when(jwtTokenUtil.validateToken(token, userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn((Collection) authorities);
        when(webFilterChain.filter(serverWebExchange)).thenReturn(Mono.empty());

        // WHEN
        Mono<Void> filtering = jwtReactiveRequestFilter.filter(serverWebExchange, webFilterChain);

        // THEN
        StepVerifier.create(filtering)
            .expectAccessibleContext()
            .then()
            .verifyComplete();
    }

    @Test
    public void invalidUserUuid_filterTest() {
        // GIVEN
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverHttpRequest.getCookies()).thenReturn(ckieMap);
        when(jwtTokenUtil.getEmailFromToken(token)).thenReturn("abc");
        when(jwtTokenUtil.getUuidFromToken(token)).thenReturn("c9029adc-d747-4116-b7bc-df0372c7f8b1");
        when(userService.findUserByEmail("abc")).thenReturn(Mono.just(user));
        when(webFilterChain.filter(serverWebExchange)).thenReturn(Mono.empty());

        // WHEN
        Mono<Void> filtering = jwtReactiveRequestFilter.filter(serverWebExchange, webFilterChain);

        // THEN
        StepVerifier.create(filtering)
            .expectNoAccessibleContext()
            .verifyComplete();
    }
}