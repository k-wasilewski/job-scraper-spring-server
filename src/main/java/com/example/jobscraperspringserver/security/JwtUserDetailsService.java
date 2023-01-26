package com.example.jobscraperspringserver.security;

import com.example.jobscraperspringserver.services.UserService;
import com.example.jobscraperspringserver.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;
import reactor.core.publisher.Mono;

@Component
public class JwtUserDetailsService implements ReactiveUserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        System.out.println("user details called");
        User user = userService.findUserByEmail(email).block();
        if (user == null) throw new UsernameNotFoundException(email);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("user"));

        String encodedPassword = user.getPassword();

        return Mono.just(new org.springframework.security.core.userdetails.User(
                email, encodedPassword, grantedAuthorities));
    }
}