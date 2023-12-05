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

    @BeforeEach
    void setUp() {
        gameTutorialRepository.deleteAll().block();

        GameTutorialDocument gameTutorialDocument1 = new GameTutorialDocument(uuidGameTutorialDocument1, "Craps",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit...");
        GameTutorialDocument gameTutorialDocument2 = new GameTutorialDocument(uuidGameTutorialDocument2, "SixDice",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium...");

        gameTutorialRepository.saveAll(Flux.just(gameTutorialDocument1, gameTutorialDocument2 )).blockLast();
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

    @DisplayName("Exists by GameName Test")
    @Test
    void existsByGameNameTest() {
        Boolean exists = gameTutorialRepository.existsByGameName("Craps").block();
        Assertions.assertEquals(true, exists);
    }

    @DisplayName("Find by GameName Test")
    @Test
    void findByChallengeTitleTest() {

        Mono<GameTutorialDocument> gameTutorialDocument1 = gameTutorialRepository.findByGameName("Craps");
        gameTutorialDocument1.blockOptional().ifPresentOrElse(
                gameTutorialDocument -> Assertions.assertEquals("Craps", gameTutorialDocument.getGameName()),
                () -> fail("Game with name Craps not found."));

        Mono<GameTutorialDocument> gameTutorialDocument2 = gameTutorialRepository.findByGameName("SixDice");
        gameTutorialDocument2.blockOptional().ifPresentOrElse(
                gameTutorialDocument -> Assertions.assertEquals("SixDice", gameTutorialDocument.getGameName()),
                () -> fail("Game with name SixDice not found."));
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

    @DisplayName("Find All Test")
    @Test
    void findAllTest() {
        Flux<GameTutorialDocument> gameTutorialDocumentFlux = gameTutorialRepository.findAll();
        StepVerifier.create(gameTutorialDocumentFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

}