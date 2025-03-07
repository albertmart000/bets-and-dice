package com.betsanddice.craps.repository;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DataMongoTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CrapsGameRepositoryTest {

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("craps"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("craps"));
    }

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    UUID uuidCrapsGame1 = UUID.fromString("81099a9e-0d59-4571-a04c-1a1a1a1a1a1a");
    UUID uuidCrapsGame2 = UUID.fromString("26977eee-89f8-11ec-a8a3-2b2b2b2b2b2b");
    UUID uuidCrapsGame3 = UUID.fromString("fd5a1a38-23ce-47e0-a3f5-3c3c3c3c3c3c");

    UUID uuidUser = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");

    List<DiceRollDocument> diceRollsList = List.of(
            new DiceRollDocument(1, 2),
            new DiceRollDocument(3, 4)
    );

    @BeforeEach
    void setUp() {

        crapsGameRepository.deleteAll().block();

        CrapsGameDocument crapsGameDocument1 = new CrapsGameDocument(uuidCrapsGame1, uuidUser,
                LocalDateTime.now(), 7, 2, 10.0, diceRollsList);

        CrapsGameDocument crapsGameDocument2 = new CrapsGameDocument(uuidCrapsGame2, uuidUser,
                LocalDateTime.now(), 7, 2, 10.0, diceRollsList);

        CrapsGameDocument crapsGameDocument3 = new CrapsGameDocument(uuidCrapsGame3, uuidUser,
                LocalDateTime.now(), 7, 2, 10.0, diceRollsList);

        crapsGameRepository.saveAll(Flux.just(crapsGameDocument1, crapsGameDocument2, crapsGameDocument3)).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        Assertions.assertNotNull(crapsGameRepository);
    }

    @DisplayName("Find by idLanguage Test")
    @Test
    void findCrapsGameByUser_userId() {
        Flux<CrapsGameDocument> crapsGameDocumentOffset0Limit1Flux = crapsGameRepository.findByUserId(uuidUser).skip(0).take(1);
        StepVerifier.create(crapsGameDocumentOffset0Limit1Flux)
                .expectNextCount(1)
                .verifyComplete();

        Flux<CrapsGameDocument> crapsGameDocumentOffset0Limit2Flux = crapsGameRepository.findByUserId(uuidUser).skip(0).take(2);
        StepVerifier.create(crapsGameDocumentOffset0Limit2Flux)
                .expectNextCount(2)
                .verifyComplete();

        Flux<CrapsGameDocument> crapsGameDocumentOffset1Limit1Flux = crapsGameRepository.findByUserId(uuidUser).skip(1).take(1);
        StepVerifier.create(crapsGameDocumentOffset1Limit1Flux)
                .expectNextCount(1)
                .verifyComplete();

        Flux<CrapsGameDocument> crapsGameDocumentOffset1Limit2Flux = crapsGameRepository.findByUserId(uuidUser).skip(2).take(2);
        StepVerifier.create(crapsGameDocumentOffset1Limit2Flux)
                .expectNextCount(1)
                .verifyComplete();
    }

}