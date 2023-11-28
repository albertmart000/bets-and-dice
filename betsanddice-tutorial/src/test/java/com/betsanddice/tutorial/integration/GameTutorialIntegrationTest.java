package com.betsanddice.tutorial.integration;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.GameTutorialDto;
import com.betsanddice.tutorial.repository.GameTutorialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class GameTutorialIntegrationTest {

    private final String TUTORIAL_BASE_URL = "/betsanddice/api/v1/tutorial";

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withExposedPorts(27017)
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("tutorials"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("tutorials"));
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private GameTutorialRepository gameTutorialRepository;

    UUID uuidGameTutorial1 = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
    UUID uuidGameTutorial2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");

    GameTutorialDto gameTutorialDtoByName = new GameTutorialDto();

    @BeforeEach
    void setUp() {

        gameTutorialRepository.deleteAll().block();

        GameTutorialDocument gameTutorial1 = new GameTutorialDocument(uuidGameTutorial1, "Craps", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur...");
        GameTutorialDocument gameTutorial2 = new GameTutorialDocument(uuidGameTutorial2, "SixDice", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt...");

        gameTutorialRepository.saveAll(Flux.just(gameTutorial1, gameTutorial2)).blockLast();
    }

    @Test
    void test() {
        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(String::toString, equalTo("Hello from Tutorial!!!"));
    }

    @Test
    void addGameTutorialTest() {
        UUID uuidGameTutorialDocument = UUID.fromString("660e1b18-0c0a-4262-a28a-85de9df6ac5f");
        gameTutorialDtoByName = new GameTutorialDto("Name", "rules");
        GameTutorialDto gameTutorialDto = new GameTutorialDto(uuidGameTutorialDocument, gameTutorialDtoByName.getGameName(),
                gameTutorialDtoByName.getRules());

        webTestClient.post()
                .uri(TUTORIAL_BASE_URL + "/gameTutorials", gameTutorialDtoByName)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(gameTutorialDto), GameTutorialDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .equals(Mono.just(gameTutorialDto).block());
    }

    @Test
    void getOneGameTutorial_InvalidId_GameTutorialNotFoundReturned() {
        String invalidUuid = "ce020780-1a66-bec4-284c8ca80296";
        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/gameTutorials/{gameTutorialUuid}", invalidUuid)
                .exchange()
                .expectStatus()
                .isEqualTo(BAD_REQUEST);
    }

    @Test
    void getOneGameTutorial_ValidId_GameTutorialReturned() {
        String validUuid = "c8a5440d-6466-463a-bccc-7fefbe9396e4";
        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/gameTutorials/{gameTutorialUuid}", validUuid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameTutorialDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void getAllUsers_UsersExist_UsersReturned() {
        webTestClient.get()
                .uri(TUTORIAL_BASE_URL + "/gameTutorials")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameTutorialDto.class)
                .hasSize(2);
    }

}