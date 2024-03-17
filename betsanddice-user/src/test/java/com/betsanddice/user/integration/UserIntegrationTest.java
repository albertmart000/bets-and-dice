package com.betsanddice.user.integration;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserIntegrationTest {

    private final String USER_BASE_URL = "/betsanddice/api/v1/user";

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withExposedPorts(27017)
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("users"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("users"));
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    UUID uuidUser1 = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");
    UUID uuidUser2 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120003");

    @BeforeEach
    void setUp() {

        userRepository.deleteAll().block();

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

    @Test
    void test() {
        webTestClient.get()
                .uri(USER_BASE_URL + "/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(String::toString, equalTo("Hello from Bets And Dice!!!"));
    }

    @Test
    void getOneUser_ValidId_UserReturned() {
        String VALID_UUID = "81099a9e-0d59-4571-a04c-31a08a711e3b";
        webTestClient.get()
                .uri(USER_BASE_URL + "/users/{userUuid}", VALID_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void getOneUser_InvalidId_UserNotFoundReturned() {
        String INVALID_UUID = "ce020780-1a66-4587-bec4-284c8ca80296";
        webTestClient.get()
                .uri(USER_BASE_URL + "/users/{userUuid}", INVALID_UUID)
                .exchange()
                .expectStatus()
                .isEqualTo(BAD_REQUEST);
    }

    @Test
    void getUserByPages_ValidPageParameters_UsersReturned() {
        webTestClient
                .get()
                .uri(USER_BASE_URL + "/users?offset=0&limit=1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .contains(new UserDto[]{})
                .hasSize(1);
    }

    @Test
    void getUsersByPages_NullPageParameters_UsersReturned() {
        webTestClient
                .get()
                .uri(USER_BASE_URL + "/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .contains(new UserDto[]{})
                .hasSize(2);
    }

}