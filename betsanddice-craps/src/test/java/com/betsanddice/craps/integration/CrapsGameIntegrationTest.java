package com.betsanddice.craps.integration;


import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.*;
import com.betsanddice.craps.repository.CrapsGameRepository;
import org.junit.jupiter.api.*;
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

    private final String CRAPS_BASE_URL = "/api";

    String uuidUser = ("706507d4-b89f-41eb-a7eb-41838d08a08f");

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

        ResultCrapsGameDto resultDto = new ResultCrapsGameDto(expectedAttempts, true, 1.671898516448076, 16.71898516448076);

        crapsGameDto1 = new CrapsGameDto(uuidCrapsGame1, uuidUser, "2023-01-31 12:00:00",
                expectedDiceSum, expectedAttempts, amountBet, diceRollsList, resultDto);
        crapsGameDto2 = new CrapsGameDto(uuidCrapsGame2, uuidUser, "2023-01-31 12:00:00",
                expectedDiceSum, expectedAttempts, amountBet, diceRollsList, resultDto);
        crapsGameDto3 = new CrapsGameDto(uuidCrapsGame2, uuidUser, "2023-01-31 12:00:00",
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
                .expectBodyList(GenericResultDto.class)
                .contains(new GenericResultDto[]{})
                .hasSize(1);
    }

    @Test
    void getUserCrapsGameStats_CrapsGameReturned() {
        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesStatsByUser/{userId}?", uuidUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserCrapsGameStatsDto.class)
                .contains(new UserCrapsGameStatsDto[]{})
                .hasSize(1);
    }

    @Test
    void deleteCrapsGamesByUserId_CrapsGameDeleted() {
        webTestClient.delete()
                .uri(CRAPS_BASE_URL + "/crapsGames/deleteCrapsGamesByUser/{userId}?", uuidUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DeleteResponseDto.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals("CrapsGames deleted successfully.", response.getMessage());
                });
    }
}