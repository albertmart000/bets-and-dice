package com.betsanddice.game.controller;

import com.betsanddice.game.dto.CrapsGameDto;
import com.betsanddice.game.dto.GameDto;
import com.betsanddice.game.service.IGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

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

    GameDto gameDto1 = new GameDto();
    GameDto gameDto2 = new GameDto();
    GameDto[] expectedGames = {gameDto1, gameDto2};

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID uuidGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID uuidGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        UUID uuidTutorial1 = UUID.fromString("990b9699-75cf-42b5-aa0f-cf3a2169c770");
        UUID uuidTutorial2 = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID uuidStat1 = UUID.fromString("76cb3a63-bc48-4b68-a291-18140a3794d7");
        UUID uuidStat2 = UUID.fromString("795e8c10-d68a-47a0-8f37-79736d6aa632");

        gameDto1 = new GameDto(uuidGame1, "Craps", uuidTutorial1, uuidStat1);
        gameDto2 = new GameDto(uuidGame2, "SixDice", uuidTutorial2, uuidStat2);
    }

    @Test
    void testHello() {
        webTestClient.get()
                .uri(GAME_BASE_URL + "/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from Game!!!");
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
        Flux<GameDto> expectedGamesFlux = Flux.just(expectedGames);

        when(gameService.getAllGames()).thenReturn(expectedGamesFlux);

        webTestClient.get()
                .uri(GAME_BASE_URL + "/games")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class);
    }
}