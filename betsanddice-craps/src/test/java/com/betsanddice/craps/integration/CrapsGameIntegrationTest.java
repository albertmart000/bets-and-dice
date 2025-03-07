package com.betsanddice.craps.integration;


import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.ResultDto;
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
import reactor.core.publisher.Flux;
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

    private final String CRAPS_BASE_URL = "/betsanddice/api/v1/craps";

    String uuidUser = "81099a9e-0d59-4571-a04c-31a08a711e3b";

    UUID uuidCrapsGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf111");
    UUID uuidCrapsGame2 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf222");
    UUID uuidCrapsGame3 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf333");

    CrapsGameDocument crapsGameDocument1 = new CrapsGameDocument();
    CrapsGameDocument crapsGameDocument2 = new CrapsGameDocument();
    CrapsGameDocument crapsGameDocument3 = new CrapsGameDocument();

    CrapsGameDto crapsGameDto1 = new CrapsGameDto();
    CrapsGameDto crapsGameDto2 = new CrapsGameDto();
    CrapsGameDto crapsGameDto3 = new CrapsGameDto();

    @BeforeEach
    void setUp() {
        crapsGameRepository.deleteAll().block();

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        int expectedDiceSum = 7;
        int expectedAttempts = 5;
        double amountBet = 10.0;
        LocalDateTime date = LocalDateTime.of(2023, 1, 31, 12, 0, 0);

        List<DiceRollDocument> diceRollsList = List.of(
                new DiceRollDocument(1, 2),
                new DiceRollDocument(3, 4));
        crapsGameDocument1 = new CrapsGameDocument(uuidCrapsGame1, uuidUser, date,
                expectedDiceSum, expectedAttempts, amountBet, diceRollsList);
        crapsGameDocument2 = new CrapsGameDocument(uuidCrapsGame2, uuidUser, date,
                expectedDiceSum, expectedAttempts, amountBet, diceRollsList);
        crapsGameDocument3 = new CrapsGameDocument(uuidCrapsGame3, uuidUser, date,
                expectedDiceSum, expectedAttempts, amountBet, diceRollsList);

        ResultDto resultDto = new ResultDto(expectedAttempts, true, 1.671898516448076, 16.71898516448076);

        crapsGameDto1 = new CrapsGameDto(uuidCrapsGame1, uuidUser, "2023-01-31 12:00:00",
                expectedDiceSum, expectedAttempts, amountBet, diceRollsList, resultDto);
        crapsGameDto2 = new CrapsGameDto(uuidCrapsGame2, uuidUser, "2023-01-31 12:00:00",
                expectedDiceSum, expectedAttempts, amountBet, diceRollsList, resultDto);
        crapsGameDto1 = new CrapsGameDto(uuidCrapsGame2, uuidUser, "2023-01-31 12:00:00",
                expectedDiceSum, expectedAttempts, amountBet, diceRollsList, resultDto);

        crapsGameRepository.saveAll(Flux.just(crapsGameDocument1, crapsGameDocument2, crapsGameDocument3)).blockLast();
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
        webTestClient.post()
                .uri(CRAPS_BASE_URL + "/crapsGames/playAndBet/{userId}", uuidUser)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(crapsGameDocument1), CrapsGameDocument.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .equals(Mono.just(crapsGameDocument1).block());
    }

    @Test
    void getCrapsGameByUser_ValidPageParameters_CrapsGameReturned() {
        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesByUser/{userId}?offset=0&limit=3", uuidUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class)
                .contains(new CrapsGameDto[]{})
                .hasSize(1);
    }
}