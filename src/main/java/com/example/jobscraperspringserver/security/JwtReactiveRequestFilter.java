package com.example.jobscraperspringserver.security;

import com.example.jobscraperspringserver.services.UserService;
import com.example.jobscraperspringserver.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

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
        System.out.println("filter called");    
        ServerHttpRequest request = serverWebExchange.getRequest();

        final String jwtToken = getCookieValue(request, "authToken");
        String email = null;
        String uuid = null;

        try {
            email = jwtTokenUtil.getEmailFromToken(jwtToken);
            uuid = jwtTokenUtil.getUuidFromToken(jwtToken);
            User user = userService.findUserByEmail(email).block();
            if (user == null || uuid == null || !user.getUuid().equals(uuid)) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token has expired");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails =
                    this.jwtUserDetailsService.findByUsername(email).block();

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(request);

                SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
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