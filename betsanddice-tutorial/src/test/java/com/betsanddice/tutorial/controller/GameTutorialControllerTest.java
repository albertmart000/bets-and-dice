package com.betsanddice.tutorial.controller;

import com.betsanddice.tutorial.dto.GameTutorialDto;
import com.betsanddice.tutorial.service.IGameTutorialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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

    @MockBean
    private DiscoveryClient discoveryClient;

    UUID uuidGameTutorialDocument1 = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
    UUID uuidGameTutorialDocument2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");

    GameTutorialDto gameTutorialDto1 = new GameTutorialDto();
    GameTutorialDto gameTutorialDto2 = new GameTutorialDto();
    GameTutorialDto[] expectedGameTutorials = {gameTutorialDto1, gameTutorialDto2};

    GameTutorialDto gameTutorialDtoToAdd = new GameTutorialDto();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        gameTutorialDto1 = new GameTutorialDto(uuidGameTutorialDocument1, "Craps",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmo…");
        gameTutorialDto2 = new GameTutorialDto(uuidGameTutorialDocument2, "SixDice",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusan…");

        gameTutorialDtoToAdd = new GameTutorialDto("Craps",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmo…");
    }

    @Test
    void testHello() {
        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from Tutorial!!!");
    }

    @Test
    void getOneGameTutorial_ValidId_GameTutorialReturned() {
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
    void getAllGameTutorials_GameTutorialsExist_GamesTutorialsReturned() {
        Flux<GameTutorialDto> expectedGameTutorialsFlux = Flux.just(expectedGameTutorials);

        when(gameTutorialService.getAllGameTutorials()).thenReturn(expectedGameTutorialsFlux);

        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/gameTutorials")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameTutorialDto.class);
    }

    @Test
    void addGameTutorialTest() {
        UUID uuidGameTutorialDocument = UUID.fromString("660e1b18-0c0a-4262-a28a-85de9df6ac5f");
        GameTutorialDto gameTutorialDto = new GameTutorialDto(uuidGameTutorialDocument, gameTutorialDtoToAdd.getGameName(),
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

}