package com.betsanddice.game.integration;

import com.betsanddice.game.document.GameDocument;
import com.betsanddice.game.dto.GameDto;
import com.betsanddice.game.repository.GameRepository;
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

import java.time.Duration;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class GameIntegrationTest {

    private final String GAME_BASE_URL = "/betsanddice/api/v1/game";

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withExposedPorts(27017)
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("games"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("games"));
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll().block();

        UUID uuidGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID uuidGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        UUID uuidTutorial1 = UUID.fromString("990b9699-75cf-42b5-aa0f-cf3a2169c770");
        UUID uuidTutorial2 = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID uuidStat1 = UUID.fromString("76cb3a63-bc48-4b68-a291-18140a3794d7");
        UUID uuidStat2 = UUID.fromString("795e8c10-d68a-47a0-8f37-79736d6aa632");

        GameDocument gameDocument1 = new GameDocument (uuidGame1, "Craps", uuidTutorial1, uuidStat1);
        GameDocument gameDocument2 = new GameDocument (uuidGame2, "SixDice", uuidTutorial2, uuidStat2);

        gameRepository.saveAll(Flux.just(gameDocument1, gameDocument2)).blockLast();
    }

    @Test
    void test() {
        webTestClient.get()
                .uri(GAME_BASE_URL + "/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(String::toString, equalTo("Hello from Game!!!"));
    }

    @Test
    void getAllGames_GamesExist_GamesReturned() {
        webTestClient.get()
                .uri(GAME_BASE_URL + "/games")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameDto.class)
                .hasSize(2);
    }
}