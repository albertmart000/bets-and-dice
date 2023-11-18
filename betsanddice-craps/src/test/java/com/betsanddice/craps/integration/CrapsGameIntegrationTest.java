package com.betsanddice.craps.integration;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.DiceRollDto;
import com.betsanddice.craps.repository.CrapsGameRepository;
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
        System.out.println("container url: {}" + container.getReplicaSetUrl("craps"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("craps"));
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

    UUID uuidCrapsGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
    UUID uuidCrapsGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

    CrapsGameDocument crapsGameDocument1 = new CrapsGameDocument();
    CrapsGameDocument crapsGameDocument2 = new CrapsGameDocument();

    @BeforeEach
    void setUp() {
        crapsGameRepository.deleteAll().block();

        DiceRollDto diceRollDto1= new DiceRollDto( 1, 2, 3);
        DiceRollDto diceRollDto2= new DiceRollDto( 3, 4, 7);
        List<DiceRollDto> diceRollsList = List.of(diceRollDto1, diceRollDto2);

        LocalDateTime date = LocalDateTime.of(2023, 1, 31, 12, 0, 0);

        crapsGameDocument1 = new CrapsGameDocument(uuidCrapsGame1, uuidUser, date, 2, diceRollsList);
        crapsGameDocument2 = new CrapsGameDocument(uuidCrapsGame2, uuidUser, date, 2, diceRollsList);

        crapsGameRepository.saveAll(Flux.just(crapsGameDocument1, crapsGameDocument2)).blockLast();
    }

    @Test
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
    void getAllCrapsGames_CrapsGamesExist_CrapsReturned() {
        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class)
                .hasSize(2);
    }

    @Test
    void addCrapsGameToUserTest() {
        webTestClient.post()
                .uri(CRAPS_BASE_URL + "/crapsGames/{userId}", uuidUser)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(crapsGameDocument1), CrapsGameDocument.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
                //.equals(Mono.just(crapsGameDocument1).block());
    }

}