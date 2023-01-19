package com.example.jobscraperspringserver.security;

import java.io.IOException;
import java.io.Serializable;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint, Serializable {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
        System.out.println(authException.getMessage());
        return Mono.error(new Exception("Unauthorized"));
    }
}