package com.betsanddice.user.repository;

import com.betsanddice.user.document.UserDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@DataMongoTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserRepositoryTest {

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("users"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("users"));
    }

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        userRepository.deleteAll().block();

        UUID uuidUser1 = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");
        UUID uuidUser2 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120003");

        UUID uuidGame1 = UUID.fromString("dcacb291-b4aa-4029-8e9b-284c8ca80296");
        UUID uuidGame2 = UUID.fromString("09fabe32-7362-4bfb-ac05-b7bf854c6e0f");
        List<UUID> gameList = List.of(uuidGame1, uuidGame2);

        UUID uuidStatistics1 = UUID.fromString("70e9755e-9e83-41d3-853a-665f1f2a2f5c");
        UUID uuidStatistics2 = UUID.fromString("bb7897b8-517d-4843-8c17-e347aba086ca");
        List<UUID> statisticsList = List.of(uuidStatistics1, uuidStatistics2);

        UserDocument user1 = new UserDocument(uuidUser1, "Morrow", "Montgomery", LocalDate.now(),
                "Player2", "morrowmontgomery@email.com", "player2", LocalDateTime.now(), "level",
                BigDecimal.valueOf(100), gameList, statisticsList);

        UserDocument user2 = new UserDocument(uuidUser2, "Morrow", "Montgomery", LocalDate.now(),
                "Player2", "morrowmontgomery@email.com", "player2", LocalDateTime.now(), "level",
                BigDecimal.valueOf(100), gameList, statisticsList);

        userRepository.saveAll(Flux.just(user1, user2)).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        assertNotNull(userRepository);
    }

    @DisplayName("Find All Test")
    @Test
    void findAllTest() {
        Flux<UserDocument> users = userRepository.findAll();
        StepVerifier.create(users)
                .expectNextCount(2)
                .verifyComplete();
    }

}