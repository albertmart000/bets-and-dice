package com.betsanddice.game.repository;

import com.betsanddice.game.document.GameDocument;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.fail;

@DataMongoTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class GameRepositoryTest {
    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("games"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("games"));
    }

    @Autowired
    private  GameRepository gameRepository;

    UUID uuidGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
    UUID uuidGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

    UUID uuidTutorial1 = UUID.fromString("990b9699-75cf-42b5-aa0f-cf3a2169c770");
    UUID uuidTutorial2 = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

    UUID uuidStat1 = UUID.fromString("76cb3a63-bc48-4b68-a291-18140a3794d7");
    UUID uuidStat2 = UUID.fromString("795e8c10-d68a-47a0-8f37-79736d6aa632");

    @BeforeEach

    public void setUp() {
        gameRepository.deleteAll().block();

        GameDocument gameDocument1 = new GameDocument (uuidGame1, "Craps", uuidTutorial1, uuidStat1);
        GameDocument gameDocument2 = new GameDocument (uuidGame2, "SixDice", uuidTutorial2, uuidStat2);

        gameRepository.saveAll(Flux.just(gameDocument1, gameDocument2)).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        Assertions.assertNotNull(gameRepository);
    }

    @DisplayName("Exists by UUID Test")
    @Test
    void existsByUuidTest() {
        Boolean exists = gameRepository.existsByUuid(uuidGame1).block();
        Assertions.assertEquals(true, exists);
    }

    @DisplayName("Find by UUID Test")
    @Test
    void findByUuidTest() {
        Mono<GameDocument> gameDocument1 = gameRepository.findByUuid(uuidGame1);
        gameDocument1.blockOptional().ifPresentOrElse(
                gameDocument -> Assertions.assertEquals(uuidGame1, gameDocument.getUuid()),
                () -> fail("Game not found: " + uuidGame1));

        Mono<GameDocument> gameDocument2 = gameRepository.findByUuid(uuidGame2);
        gameDocument2.blockOptional().ifPresentOrElse(
                gameDocument -> Assertions.assertEquals(uuidGame2, gameDocument.getUuid()),
                () -> fail("Game not found: " + uuidGame2));
    }

    @DisplayName("Find All Test")
    @Test
    void findAllTest() {
        Flux<GameDocument> gameDocumentFlux= gameRepository.findAll();
        StepVerifier.create(gameDocumentFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}