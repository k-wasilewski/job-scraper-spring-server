package com.example.jobscraperspringserver.security;

import java.io.Serializable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint, Serializable {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
        return Mono.error(new Exception("Unauthorized"));
    }
}