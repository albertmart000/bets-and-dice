package com.betsanddice.auth.service;

import com.betsanddice.auth.dto.User;
import com.betsanddice.auth.exception.CustomBadRequestException;
import com.betsanddice.auth.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService, ReactiveUserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final WebClient webClient;

    private final String USER_BASE_URL = "/api";

    @Override
    public Mono<User> fetchUserData(String email) {
        return webClient.get()
                .uri(USER_BASE_URL + "/users/userByEmail/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new CustomBadRequestException("400 - " + errorBody))))
                .bodyToMono(User.class);
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return fetchUserData(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with email: " + email)))
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole()))
                ));
    }

}