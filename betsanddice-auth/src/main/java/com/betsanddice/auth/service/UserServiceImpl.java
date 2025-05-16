package com.betsanddice.auth.service;

import com.betsanddice.auth.dto.User;
import com.betsanddice.auth.exception.CustomBadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final WebClient webClient;

    private final String USER_BASE_URL = "/api";

    public UserServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

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
}