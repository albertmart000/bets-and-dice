package com.betsanddice.tutorial.repository;

import com.betsanddice.tutorial.document.GameTutorialDocument;
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
class GameTutorialRepositoryTest {

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("tutorials"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("tutorials"));
    }

    @Autowired
    private GameTutorialRepository gameTutorialRepository;

    UUID uuidGameTutorialDocument1 = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");
    UUID uuidGameTutorialDocument2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");
    UUID uuidGameTutorialDocument3 = UUID.fromString("fb1ed64e-fd21-4a0f-af46-327ad7d1fe5d");

    UUID uuidGameDocument1 = UUID.fromString("7f9dcc63-6daf-4ba2-b3c7-e0b59534f856");
    UUID uuidGameDocument2 = UUID.fromString("bf71596f-0dff-4ce7-b6d6-e348fbf914ed");
    UUID uuidGameDocument3 = UUID.fromString("f9ce5b99-bcf8-4af3-a4c7-db2beb3994b9");

    @BeforeEach
    void setUp() {
        gameTutorialRepository.deleteAll().block();

        GameTutorialDocument gameTutorialDocument1 = new GameTutorialDocument(uuidGameTutorialDocument1, uuidGameDocument1, "Craps",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit...");
        GameTutorialDocument gameTutorialDocument2 = new GameTutorialDocument(uuidGameTutorialDocument2, uuidGameDocument2,"SixDice",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium...");
        GameTutorialDocument gameTutorialDocument3 = new GameTutorialDocument(uuidGameTutorialDocument3, uuidGameDocument3,"Poker",
                "At vero eos et accusamus et iusto odio dignissimos ducimus qui blandit...");

        gameTutorialRepository.saveAll(Flux.just(gameTutorialDocument1, gameTutorialDocument2, gameTutorialDocument3 )).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        Assertions.assertNotNull(gameTutorialRepository);
    }

    @DisplayName("Exists by UUID Test")
    @Test
    void existsByUuidTest() {
        Boolean exists = gameTutorialRepository.existsByUuid(uuidGameTutorialDocument1).block();
        Assertions.assertEquals(true, exists);
    }

    @DisplayName("Exists by GameId Test")
    @Test
    void existsByGameIdTest() {
        Boolean exists = gameTutorialRepository.existsByGameId(uuidGameDocument1).block();
        Assertions.assertEquals(true, exists);
    }

    @DisplayName("Exists by GameName Test")
    @Test
    void existsByGameNameTest() {
        Boolean exists = gameTutorialRepository.existsByGameName("Craps").block();
        Assertions.assertEquals(true, exists);
    }

    @DisplayName("Find by UUID Test")
    @Test
    void findByUuidTest() {
        Mono<GameTutorialDocument> gameTutorialDocument1 = gameTutorialRepository.findByUuid(uuidGameTutorialDocument1);
        gameTutorialDocument1.blockOptional().ifPresentOrElse(
                gameTutorialDocument -> Assertions.assertEquals(uuidGameTutorialDocument1, gameTutorialDocument.getUuid()),
                () -> fail("Game Tutorial not found: " + uuidGameTutorialDocument1));

        Mono<GameTutorialDocument> gameTutorialDocument2 = gameTutorialRepository.findByUuid(uuidGameTutorialDocument2);
        gameTutorialDocument2.blockOptional().ifPresentOrElse(
                gameTutorialDocument -> Assertions.assertEquals(uuidGameTutorialDocument2, gameTutorialDocument.getUuid()),
                () -> fail("Game Tutorial not found: " + uuidGameTutorialDocument2));
    }

    @DisplayName("Find by GameId Test")
    @Test
    void findByGameIdTest() {
        Mono<GameTutorialDocument> gameTutorialDocument1 = gameTutorialRepository.findByGameId(uuidGameDocument1);
        gameTutorialDocument1.blockOptional().ifPresentOrElse(
                gameTutorialDocument -> Assertions.assertEquals(uuidGameDocument1, gameTutorialDocument.getGameId()),
                () -> fail("Game Tutorial not found: " + uuidGameDocument1));

        Mono<GameTutorialDocument> gameTutorialDocument2 = gameTutorialRepository.findByGameId(uuidGameDocument2);
        gameTutorialDocument2.blockOptional().ifPresentOrElse(
                gameTutorialDocument -> Assertions.assertEquals(uuidGameDocument2, gameTutorialDocument.getGameId()),
                () -> fail("Game Tutorial not found: " + uuidGameDocument2));
    }

    @DisplayName("Find by GameName Test")
    @Test
    void findByGameNameTest() {

        Mono<GameTutorialDocument> gameTutorialDocument1 = gameTutorialRepository.findByGameName("Craps");
        gameTutorialDocument1.blockOptional().ifPresentOrElse(
                gameTutorialDocument -> Assertions.assertEquals("Craps", gameTutorialDocument.getGameName()),
                () -> fail("Game with name Craps not found."));

        Mono<GameTutorialDocument> gameTutorialDocument2 = gameTutorialRepository.findByGameName("SixDice");
        gameTutorialDocument2.blockOptional().ifPresentOrElse(
                gameTutorialDocument -> Assertions.assertEquals("SixDice", gameTutorialDocument.getGameName()),
                () -> fail("Game with name SixDice not found."));
    }

    @DisplayName("Find Game Tutorials for a Page Test")
    @Test
    void findAllTest() {

        Flux<GameTutorialDocument> usersOffset0Limit1Flux = gameTutorialRepository.findAllByUuidNotNull().skip(0).take(1);
        StepVerifier.create(usersOffset0Limit1Flux)
                .expectNextCount(1)
                .verifyComplete();

        Flux<GameTutorialDocument> usersOffset0Limit2Flux = gameTutorialRepository.findAllByUuidNotNull().skip(0).take(2);
        StepVerifier.create(usersOffset0Limit2Flux)
                .expectNextCount(2)
                .verifyComplete();

        Flux<GameTutorialDocument> usersOffset1Limit1Flux = gameTutorialRepository.findAllByUuidNotNull().skip(1).take(1);
        StepVerifier.create(usersOffset1Limit1Flux)
                .expectNextCount(1)
                .verifyComplete();

        Flux<GameTutorialDocument> usersOffset1Limit2Flux = gameTutorialRepository.findAllByUuidNotNull().skip(2).take(2);
        StepVerifier.create(usersOffset1Limit2Flux)
                .expectNextCount(1)
                .verifyComplete();
    }

}