package com.betsanddice.game.controller;

import com.betsanddice.game.dto.CrapsGameDto;
import com.betsanddice.game.dto.GameDto;
import com.betsanddice.game.service.IGameService;
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

@WebFluxTest(GameController.class)
class GameControllerTest {

    private final String GAME_BASE_URL = "/betsanddice/api/v1/game";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IGameService gameService;

    @MockBean
    private DiscoveryClient discoveryClient;

    @Test
    void test() {
        List<ServiceInstance> instances = Arrays.asList(
                new DefaultServiceInstance("instanceId", "betsanddice-user", "localhost", 8080, false),
                new DefaultServiceInstance("instanceId", "betsanddice-tutorial", "localhost", 8081, false)
        );

        when(discoveryClient.getInstances("betsanddice-user")).thenReturn(instances);
        when(discoveryClient.getInstances("betsanddice-tutorial")).thenReturn(Collections.singletonList(instances.get(1)));

        webTestClient.get()
                .uri(GAME_BASE_URL + "/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from Bets And Dice!!!");
    }

    @Test
    void getOneGame_ValidUuid_GameReturned() {
        String gameUuid = "valid-game-uuid";
        GameDto expectedGameDto = new GameDto();

        when(gameService.getGameByUuid(gameUuid))
                .thenReturn(Mono.just(expectedGameDto));

        webTestClient.get()
                .uri(GAME_BASE_URL + "/games/{gameUuid}", gameUuid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void getAllGames_GamesExist_GamesReturned() {
        GameDto gameDto1 = new GameDto();
        GameDto gameDto2 = new GameDto();
        GameDto[] expectedGames = {gameDto1, gameDto2};

        Flux<GameDto> expectedGamesFlux = Flux.just(expectedGames);

        when(gameService.getAllGames()).thenReturn(expectedGamesFlux);

        webTestClient.get()
                .uri(GAME_BASE_URL + "/games")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class);
    }
}