package com.betsanddice.user.controller;

import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IUserService userService;

    @MockBean
    private DiscoveryClient discoveryClient;

    @Test
    void test() {
        List<ServiceInstance> instances = Arrays.asList(
                new DefaultServiceInstance("instanceId", "betsanddice-user", "localhost", 8080, false),
                new DefaultServiceInstance("instanceId", "betsanddice-craps", "localhost", 8081, false)
        );

        when(discoveryClient.getInstances("betsanddice-user")).thenReturn(instances);
        when(discoveryClient.getInstances("betsanddice-craps")).thenReturn(Collections.singletonList(instances.get(1)));

        webTestClient.get().uri("/betsanddice/api/v1/user/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello from Bets And Dice!!!");
    }

    @Test
    void getOneUser_ValidId_ChallengeReturned() {
        String userUuid = "valid-user-uuid";
        UserDto expectedUserDto = new UserDto();

        when(userService.getUserByUuid(userUuid)).thenReturn(Mono.just(expectedUserDto));

        webTestClient.get()
                .uri("/betsanddice/api/v1/user/users/{userUuid}", userUuid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void getAllChallenges_ChallengesExist_ChallengesReturned() {
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        UserDto[] expectedUsers = {userDto1, userDto2};
        Flux<UserDto> expectedUsersFlux = Flux.just(expectedUsers);

        when(userService.getAllUsers()).thenReturn(expectedUsersFlux);

        webTestClient.get()
                .uri("/betsanddice/api/v1/user/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class);
    }

}