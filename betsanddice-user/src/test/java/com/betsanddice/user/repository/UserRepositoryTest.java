package com.betsanddice.user.repository;

import com.betsanddice.user.document.UserDocument;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.fail;

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

    UUID uuidUser1 = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");
    UUID uuidUser2 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120003");
    UUID uuidUser3 = UUID.fromString("fd5a1a38-23ce-47e0-a3f5-4a9148eff504");

    @BeforeEach
    public void setUp() {

        userRepository.deleteAll().block();

        UUID uuidGame1 = UUID.fromString("dcacb291-b4aa-4029-8e9b-284c8ca80296");
        UUID uuidGame2 = UUID.fromString("09fabe32-7362-4bfb-ac05-b7bf854c6e0f");
        List<UUID> gameList = List.of(uuidGame1, uuidGame2);

        UUID uuidStatistics1 = UUID.fromString("70e9755e-9e83-41d3-853a-665f1f2a2f5c");
        UUID uuidStatistics2 = UUID.fromString("bb7897b8-517d-4843-8c17-e347aba086ca");
        List<UUID> statisticsList = List.of(uuidStatistics1, uuidStatistics2);

        UserDocument user1 = new UserDocument(uuidUser1, "Morrow", "Montgomery", LocalDate.now(),
                "Player1", "morrowmontgomery@email.com", "player1", LocalDateTime.now(), "level",
                BigDecimal.valueOf(100), gameList, statisticsList);

        UserDocument user2 = new UserDocument(uuidUser2, "Morrow", "Montgomery", LocalDate.now(),
                "Player2", "morrowmontgomery@email.com", "player2", LocalDateTime.now(), "level",
                BigDecimal.valueOf(100), gameList, statisticsList);

        UserDocument user3 = new UserDocument(uuidUser3, "Morrow", "Montgomery", LocalDate.now(),
                "Player3", "morrowmontgomery@email.com", "player3", LocalDateTime.now(), "level",
                BigDecimal.valueOf(100), gameList, statisticsList);

        userRepository.saveAll(Flux.just(user1, user2, user3)).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        Assertions.assertNotNull(userRepository);
    }

    @DisplayName("Find Users for a Page Test")
    @Test
    void findAllTest() {

        Flux<UserDocument> usersOffset0Limit1Flux = userRepository.findAllByUuidNotNull().skip(0).take(1);
        StepVerifier.create(usersOffset0Limit1Flux)
                .expectNextCount(1)
                .verifyComplete();

        Flux<UserDocument> usersOffset0Limit2Flux = userRepository.findAllByUuidNotNull().skip(0).take(2);
        StepVerifier.create(usersOffset0Limit2Flux)
                .expectNextCount(2)
                .verifyComplete();

        Flux<UserDocument> usersOffset1Limit1Flux = userRepository.findAllByUuidNotNull().skip(1).take(1);
        StepVerifier.create(usersOffset1Limit1Flux)
                .expectNextCount(1)
                .verifyComplete();

        Flux<UserDocument> usersOffset1Limit2Flux = userRepository.findAllByUuidNotNull().skip(2).take(2);
        StepVerifier.create(usersOffset1Limit2Flux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @DisplayName("Find by UUID Test")
    @Test
    void findByUuidTest() {

        Mono<UserDocument> user1 = userRepository.findByUuid(uuidUser1);
        user1.blockOptional().ifPresentOrElse(
                user -> Assertions.assertEquals(user.getUuid(), uuidUser1),
                () -> fail("User not found: " + uuidUser1));

        Mono<UserDocument> user2 = userRepository.findByUuid(uuidUser2);
        user2.blockOptional().ifPresentOrElse(
                user -> Assertions.assertEquals(user.getUuid(), uuidUser2),
                () -> fail("User not found: " + uuidUser2));
    }
}