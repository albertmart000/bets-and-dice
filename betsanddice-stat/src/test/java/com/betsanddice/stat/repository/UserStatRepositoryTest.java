package com.betsanddice.stat.repository;

import com.betsanddice.stat.document.UserStatByGameDocument;
import com.betsanddice.stat.document.UserStatDocument;
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
import java.util.List;
import java.util.UUID;

@DataMongoTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserStatRepositoryTest {
    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("stats"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("stats"));
    }

    @Autowired
    private UserStatRepository userStatRepository;

    @BeforeEach
    void setup() {
        userStatRepository.deleteAll().block();

        UUID uuidUserStat1 = UUID.fromString("c73a00ef-bfb1-458a-9c9d-5b1cdfca4a01");
        UUID uuidUserStat2 = UUID.fromString("76628e83-b879-4186-8263-3325236a5fa5");

        UUID uuidUser1 = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
        UUID uuidUser2 = UUID.fromString("fecf3dfe-df69-46de-ae5b-2a5989aa0c1f");

        UUID uuidUserStatByGame1 = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        UUID uuidUserStatByGame2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");
        UUID uuidUserStatByGame3 = UUID.fromString("1346c0f4-db9b-4fdb-afd0-4b161e85405d");

        UserStatByGameDocument userStatByGameDocument = new UserStatByGameDocument(uuidUserStatByGame1, uuidUserStatByGame2, uuidUserStatByGame3);
        List<UserStatByGameDocument> userGames = List.of(userStatByGameDocument);

        UserStatDocument userStatDocument1 = new UserStatDocument(uuidUserStat1, uuidUser1, "Intermediate", 50, userGames);
        UserStatDocument userStatDocument2 = new UserStatDocument(uuidUserStat2, uuidUser2, "Intermediate", 50, userGames);

        userStatRepository.saveAll(Flux.just(userStatDocument1, userStatDocument2)).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        Assertions.assertNotNull(userStatRepository);
    }

    @DisplayName("Find All Test")
    @Test
    void findAllTest() {
        Flux<UserStatDocument> userStatDocumentFlux = userStatRepository.findAll();
        StepVerifier.create(userStatDocumentFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

}