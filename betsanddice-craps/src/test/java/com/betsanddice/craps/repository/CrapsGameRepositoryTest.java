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
    private  CrapsGameRepository crapsGameRepository;

    @BeforeEach
    public void setUp() {

        crapsGameRepository.deleteAll().block();

        UUID uuidCrapsGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID uuidCrapsGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        DiceRollDocument diceRollDocument1= new DiceRollDocument( 1, 2, 3);
        DiceRollDocument diceRollDocument2= new DiceRollDocument( 3, 4, 7);
        List<DiceRollDocument> diceRollsList = List.of(diceRollDocument1, diceRollDocument2);

        CrapsGameDocument crapsGameDocument1 = new CrapsGameDocument (uuidCrapsGame1, uuidUser, LocalDateTime.now(),
                2, diceRollsList);
        CrapsGameDocument crapsGameDocument2 = new CrapsGameDocument (uuidCrapsGame2, uuidUser, LocalDateTime.now(),
                2, diceRollsList);

        crapsGameRepository.saveAll(Flux.just(crapsGameDocument1, crapsGameDocument2)).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        Assertions.assertNotNull(crapsGameRepository);
    }

    @DisplayName("Find All Test")
    @Test
    void findAllTest() {
        Flux<CrapsGameDocument> crapsGameDocumentFlux= crapsGameRepository.findAll();
        StepVerifier.create(crapsGameDocumentFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

}