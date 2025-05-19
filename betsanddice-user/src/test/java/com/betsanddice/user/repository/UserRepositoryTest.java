package com.betsanddice.user.repository;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.document.enums.Role;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private final String emailUser1 = "user1@email.com";
    private final String emailUser2 = "user2@email.com";
    private final String emailUser3 = "user3@email.com";

    private final String nicknameUser1 = "Player1";
    private final String nicknameUser2 = "Player2";
    private final String nicknameUser3 = "Player3";

    @BeforeEach
    void setUp() {

        userRepository.deleteAll().block();

        UserDocument user1 = new UserDocument(uuidUser1, "Morrow", "Montgomery", "Player1",
                emailUser1, nicknameUser1, LocalDate.now(), LocalDateTime.now(), Role.PLAYER);

        UserDocument user2 = new UserDocument(uuidUser2, "Morrow", "Montgomery", "Player2",
                emailUser2, nicknameUser2, LocalDate.now(), LocalDateTime.now(), Role.PLAYER);

        UserDocument user3 = new UserDocument(uuidUser3, "Morrow", "Montgomery", "Player3",
                emailUser3, nicknameUser3, LocalDate.now(), LocalDateTime.now(), Role.PLAYER);

        userRepository.saveAll(Flux.just(user1, user2, user3)).blockLast();
    }

    @DisplayName("Repository not null Test")
    @Test
    void testDB() {
        Assertions.assertNotNull(userRepository);
    }

    @DisplayName("Exists by UUID Test")
    @Test
    void existsByUuidTest() {
        Boolean exists = userRepository.existsByUuid(uuidUser1).block();
        Assertions.assertEquals(true, exists);
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

    @DisplayName("Find by email Test")
    @Test
    void findByEmailTest() {

        Mono<UserDocument> user1 = userRepository.findByEmail(emailUser1);
        user1.blockOptional().ifPresentOrElse(
                user -> Assertions.assertEquals(user.getEmail(), emailUser1),
                () -> fail("User not found: " + emailUser1));

        Mono<UserDocument> user2 = userRepository.findByEmail(emailUser2);
        user2.blockOptional().ifPresentOrElse(
                user -> Assertions.assertEquals(user.getEmail(), emailUser2),
                () -> fail("User not found: " + emailUser2));
    }

    @DisplayName("Find by nickname Test")
    @Test
    void findByNicknameTest() {

        Mono<UserDocument> user1 = userRepository.findByNickname(nicknameUser1);
        user1.blockOptional().ifPresentOrElse(
                user -> Assertions.assertEquals(user.getNickname(), nicknameUser1),
                () -> fail("User not found: " + nicknameUser1));

        Mono<UserDocument> user2 = userRepository.findByNickname(nicknameUser2);
        user2.blockOptional().ifPresentOrElse(
                user -> Assertions.assertEquals(user.getNickname(), nicknameUser2),
                () -> fail("User not found: " + nicknameUser2));
    }

    @DisplayName("Find Users for a Page Test")
    @Test
    void findAllTest() {

        Flux<UserDocument> usersOffset0Limit1Flux = userRepository.findAllByUuidNotNullExcludingTestingValues().skip(0).take(1);
        StepVerifier.create(usersOffset0Limit1Flux)
                .expectNextCount(1)
                .verifyComplete();

        Flux<UserDocument> usersOffset0Limit2Flux = userRepository.findAllByUuidNotNullExcludingTestingValues().skip(0).take(2);
        StepVerifier.create(usersOffset0Limit2Flux)
                .expectNextCount(2)
                .verifyComplete();

        Flux<UserDocument> usersOffset1Limit1Flux = userRepository.findAllByUuidNotNullExcludingTestingValues().skip(1).take(1);
        StepVerifier.create(usersOffset1Limit1Flux)
                .expectNextCount(1)
                .verifyComplete();

        Flux<UserDocument> usersOffset1Limit2Flux = userRepository.findAllByUuidNotNullExcludingTestingValues().skip(2).take(2);
        StepVerifier.create(usersOffset1Limit2Flux)
                .expectNextCount(1)
                .verifyComplete();
    }
}