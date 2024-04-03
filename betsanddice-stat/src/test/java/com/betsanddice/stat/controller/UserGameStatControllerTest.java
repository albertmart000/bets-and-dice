package com.betsanddice.stat.controller;

import com.betsanddice.stat.dto.UserGameStatDto;
import com.betsanddice.stat.service.IUserGameStatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(UserGameStatController.class)
class UserGameStatControllerTest {

    private final String STAT_BASE_URL = "/betsanddice/api/v1/stat";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IUserGameStatService userStatService;

    UserGameStatDto userGameStatDto1 = new UserGameStatDto();
    UserGameStatDto userGameStatDto2 = new UserGameStatDto();

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
        UserGameStatDto[] expectedUserStats = {userGameStatDto1, userGameStatDto2};
        Flux<UserGameStatDto> expectedUserStatsFlux = Flux.just(expectedUserStats);

        when(userStatService.getAllUserStats()).thenReturn(expectedUserStatsFlux);

        webTestClient.get()
                .uri(STAT_BASE_URL + "/userStats")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserGameStatDto.class);
    }
}