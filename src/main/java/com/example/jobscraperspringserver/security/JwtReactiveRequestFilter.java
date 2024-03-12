package com.example.jobscraperspringserver.security;

import com.example.jobscraperspringserver.services.UserService;
import com.example.jobscraperspringserver.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class JwtReactiveRequestFilter implements WebFilter {
    @Autowired
    private ReactiveUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserService userService;
 
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {  
        ServerHttpRequest request = serverWebExchange.getRequest();

        final String jwtToken = getCookieValue(request, "authToken");

        try {
            final String email = jwtTokenUtil.getEmailFromToken(jwtToken);
            final String uuid = jwtTokenUtil.getUuidFromToken(jwtToken);

            return userService.findUserByEmail(email).flatMap(u -> {
                if (u == null || uuid == null || !u.getUuid().equals(uuid)) {
                    System.out.println("Unable to find a user from JWT Token");
                } else if (email != null) {
                    return this.jwtUserDetailsService.findByUsername(email).flatMap(userDetails -> {
                        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                            usernamePasswordAuthenticationToken.setDetails(request);

                            Context ctx = ReactiveSecurityContextHolder.withAuthentication(usernamePasswordAuthenticationToken);
                    
                            return webFilterChain.filter(serverWebExchange).contextWrite(ctx);
                        }

                        return webFilterChain.filter(serverWebExchange);
                    });
                }

                return webFilterChain.filter(serverWebExchange);
            });
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token has expired");
        }

        return webFilterChain.filter(serverWebExchange);
    }

    private String getCookieValue(ServerHttpRequest req, String cookieName) {
        return req.getCookies().entrySet().stream()
                .filter(c -> c.getKey().equals(cookieName))
                .findFirst()
                .map(c -> c.getValue())
                .map(c -> c.get(0))
                .map(HttpCookie::getValue)
                .orElse(null);
    }
}