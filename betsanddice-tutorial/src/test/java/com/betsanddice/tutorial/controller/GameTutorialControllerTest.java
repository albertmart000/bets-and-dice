package com.betsanddice.tutorial.controller;

import com.betsanddice.tutorial.dto.GameTutorialDto;
import com.betsanddice.tutorial.service.IGameTutorialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.when;

@WebFluxTest(GameTutorialController.class)
class GameTutorialControllerTest {

    private final String TUTORIAL_BASE_URL = "/betsanddice/api/v1/tutorial";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IGameTutorialService gameTutorialService;

    GameTutorialDto gameTutorialDto1 = new GameTutorialDto();
    GameTutorialDto gameTutorialDto2 = new GameTutorialDto();
    GameTutorialDto gameTutorialDto3 = new GameTutorialDto();

    @Test
    void test() {
        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from Tutorial!!!");
    }

    @Test
    void getOneGameTutorial_ValidUuid_GameTutorialReturned() {
        String gameTutorialUuid = "valid-game-tutorial-uuid";
        GameTutorialDto expectedGameTutorialDto = new GameTutorialDto();

        when(gameTutorialService.getGameTutorialByUuid(gameTutorialUuid))
                .thenReturn(Mono.just(expectedGameTutorialDto));

        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/gameTutorials/{gameTutorialUuid}", gameTutorialUuid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameTutorialDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void getOneGameTutorial_ValidGameId_GameTutorialReturned() {
        String gameGameId = "valid-game-uuid";
        GameTutorialDto expectedGameTutorialDto = new GameTutorialDto();

        when(gameTutorialService.getGameTutorialByGameId(gameGameId))
                .thenReturn(Mono.just(expectedGameTutorialDto));

        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/gameTutorials/game/{gameUuid}", gameGameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameTutorialDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void addGameTutorialTest() {
        GameTutorialDto gameTutorialDtoToAdd = new GameTutorialDto();

        UUID uuidGameTutorialDocument = UUID.fromString("660e1b18-0c0a-4262-a28a-85de9df6ac5f");
        UUID uuidGameDocument = UUID.fromString("c9de85c0-541e-48e6-b8ac-a9b2541231e3");
        GameTutorialDto gameTutorialDto = new GameTutorialDto(uuidGameTutorialDocument, uuidGameDocument, gameTutorialDtoToAdd.getGameName(),
                gameTutorialDtoToAdd.getRules());

        when(gameTutorialService.addGameTutorial(gameTutorialDto)).thenAnswer(x -> (Mono.just(gameTutorialDto)));

        webTestClient.post()
                .uri(TUTORIAL_BASE_URL + "/gameTutorials", gameTutorialDtoToAdd)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(gameTutorialDto), GameTutorialDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .equals(Mono.just(gameTutorialDto));
    }

    @Test
    void getAllUsers_ValidPageParameters_UsersReturned() {
        GameTutorialDto[] expectedGameTutorials = {gameTutorialDto1, gameTutorialDto2, gameTutorialDto3};
        Flux<GameTutorialDto> expectedGameTutorialsFlux = Flux.just(expectedGameTutorials);

        String offset = "0";
        String limit = "3";

        when(gameTutorialService.getAllGameTutorials(Integer.parseInt(offset), Integer.parseInt(limit)))
                .thenReturn(expectedGameTutorialsFlux);

        webTestClient.get()
                .uri("/betsanddice/api/v1/tutorial/gameTutorials?offset=0&limit=3")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameTutorialDto.class);
    }

    @Test
    void getAllUsers_NullPageParameters_UsersReturned() {
        GameTutorialDto[] expectedGameTutorials = {gameTutorialDto1, gameTutorialDto2};
        Flux<GameTutorialDto> expectedGameTutorialsFlux = Flux.just(expectedGameTutorials);

        String offset = "0";
        String limit = "2";

        when(gameTutorialService.getAllGameTutorials(Integer.parseInt(offset), Integer.parseInt(limit)))
                .thenReturn(expectedGameTutorialsFlux);

        webTestClient.get()
                .uri("/betsanddice/api/v1/tutorial/gameTutorials")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameTutorialDto.class);
    }
}