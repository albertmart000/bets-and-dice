package com.betsanddice.craps.repository;

import com.betsanddice.craps.document.CrapsDocument;
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

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.fail;

@DataMongoTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CrapsRepositoryTest {

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
    private CrapsRepository crapsRepository;

    UUID uuidCrapsDocument = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");

    @BeforeEach
    void setUp() {

        crapsRepository.deleteAll().block();

        UUID uuidUser = UUID.fromString("dcacb291-b4aa-4029-8e9b-284c8ca80296");

        UUID uuidGame1 = UUID.fromString("09fabe32-7362-4bfb-ac05-b7bf854c6e0f");
        UUID uuidGame2 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120003");
        List<UUID> gamesList = List.of(uuidGame1, uuidGame2);

        Map<UUID, List<UUID>> userGames = Collections.singletonMap(uuidUser, gamesList);

        CrapsDocument crapsDocument = new CrapsDocument(uuidCrapsDocument, "Craps", "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
                userGames);

        crapsRepository.saveAll(Flux.just(crapsDocument)).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        Assertions.assertNotNull(crapsRepository);
    }

    @DisplayName("Find by UUID Test")
    @Test
    void findByUuidTest() {

        Mono<CrapsDocument> crapsDocument = crapsRepository.findByUuid(uuidCrapsDocument);
        crapsDocument.blockOptional().ifPresentOrElse(
                crapsDocument1 -> Assertions.assertEquals(crapsDocument1.getUuid(), uuidCrapsDocument),
                () -> fail("User not found: " + uuidCrapsDocument));
    }

}