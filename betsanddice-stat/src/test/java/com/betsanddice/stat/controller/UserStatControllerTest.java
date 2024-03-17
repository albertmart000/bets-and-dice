package com.betsanddice.stat.controller;

import com.betsanddice.stat.dto.UserStatDto;
import com.betsanddice.stat.service.IUserStatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(UserStatController.class)
class UserStatControllerTest {

    private final String STAT_BASE_URL = "/betsanddice/api/v1/stat";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IUserStatService userStatService;

    UserStatDto userStatDto1 = new UserStatDto();
    UserStatDto userStatDto2 = new UserStatDto();

    @Test
    void test() {
        webTestClient.get()
                .uri(STAT_BASE_URL + "/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from Stat!!!");
    }

    @Test
    void getAllUserStats_UserStatsExist_UserStatsReturned_Test() {
        UserStatDto[] expectedUserStats = {userStatDto1, userStatDto2};
        Flux<UserStatDto> expectedUserStatsFlux = Flux.just(expectedUserStats);

        when(userStatService.getAllUserStats()).thenReturn(expectedUserStatsFlux);

        webTestClient.get()
                .uri(STAT_BASE_URL + "/userStats")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserStatDto.class);
    }
}