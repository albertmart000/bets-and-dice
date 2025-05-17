package com.betsanddice.auth.controller;

import com.betsanddice.auth.dto.User;
import com.betsanddice.auth.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthController.class)
class AuthControllerTest {

    private final String AUTH_BASE_URL = "/api";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private IUserService userService;

    @Test
    void test() {
        webTestClient.get()
                .uri(AUTH_BASE_URL + "/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from Auth!!!");
    }

    @Test
    void getUser_ValidEmail_UserReturned() {
        String email= "admin@email.com";
        User expectedUser = new User();

        when(userService.fetchUserData(email)).thenReturn(Mono.just(expectedUser));

        webTestClient.get()
                .uri(AUTH_BASE_URL + "/user/{email}", email)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(dto -> {
                    assert dto != null;
                });
    }
}