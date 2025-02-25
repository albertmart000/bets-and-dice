package com.betsanddice.craps.integration;


import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.BetDto;
import com.betsanddice.craps.repository.CrapsGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CrapsGameIntegrationTest {

    private final String CRAPS_BASE_URL = "/betsanddice/api/v1/craps";

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withExposedPorts(27017)
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("craps"));
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
    UUID uuidCrapsGame = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");

    CrapsGameDocument crapsGameDocument = new CrapsGameDocument();

    @BeforeEach
    void setUp() {
        crapsGameRepository.deleteAll().block();

        List<DiceRollDocument> diceRollsList = List.of(
                new DiceRollDocument(1, 2),
                new DiceRollDocument(3, 4));

        BetDto bet = new BetDto(7, 5, 100);

        crapsGameDocument = new CrapsGameDocument(uuidCrapsGame, uuidUser,
                LocalDateTime.now(), bet, diceRollsList);
    }

    @Test
    @DisplayName("Test response Hello")
    void test() {
        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(String::toString, equalTo("Hello from Craps!!!"));
    }

    @Test
    void playAndBetCrapsGameByUserTest() {
        uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
        webTestClient.post()
                .uri(CRAPS_BASE_URL + "/crapsGames/playAndBet/{userId}", uuidUser)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(crapsGameDocument), CrapsGameDocument.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .equals(Mono.just(crapsGameDocument).block());
    }
}