package com.betsanddice.user.integration;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.document.enums.Role;
import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.dto.UserRegisterDto;
import com.betsanddice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CONFLICT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserIntegrationTest {

    private final String USER_BASE_URL = "/api";

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

    UUID userId1 = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");
    UUID userId2 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120003");
    UUID userId3 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac333333");

    @BeforeEach
    void setUp() {
        userRepository.deleteAll().block();

        UserDocument user1 = new UserDocument(userId1, "Morrow", "Montgomery", "Player1",
                "user1@email.com", "player1", LocalDate.now(), LocalDateTime.now(), Role.PLAYER);
        UserDocument user2 = new UserDocument(userId2, "Morrow", "Montgomery", "Player2",
                "user2@email.com", "player2", LocalDate.now(), LocalDateTime.now(), Role.PLAYER);
        UserDocument user3 = new UserDocument(userId3, "Morrow", "Montgomery", "Player3",
                "user3@email.com", "player3", LocalDate.now(), LocalDateTime.now(), Role.PLAYER);

        userRepository.saveAll(Flux.just(user1, user2, user3)).blockLast();
    }

    @Test
    @DisplayName("Test response Hello")
    void test() {
        webTestClient.get()
                .uri(USER_BASE_URL + "/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(String::toString, equalTo("Hello from User!!!"));
    }

    @Test
    void registerUser_NewsAndValidParams_UserRegisteredSuccessfully() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("New", "User", "NewUser1", "new1@email.com",
                "newUser1", LocalDate.parse("2000-03-03"));

        webTestClient.post()
                .uri(USER_BASE_URL +"/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRegisterDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(userDto -> {
                    assert userDto.getEmail().equals(userRegisterDto.getEmail());
                    assert userDto.getNickname().equals(userRegisterDto.getNickname());
                });
    }

    @Test
    void registerUser_EmailExist_UserNotRegistered() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("New", "User", "NewUser1", "user1@email.com",
                "newUser1", LocalDate.parse("2000-03-03"));

        webTestClient.post()
                .uri(USER_BASE_URL +"/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRegisterDto)
                .exchange()
                .expectStatus().isEqualTo(CONFLICT)
                .expectBody(String.class)
                .value(errorMessage -> {
                    assert errorMessage.contains("User with email user1@email.com already exists");
                });
    }

    @Test
    void registerUser_NicknameExist_UserNotRegistered() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("New", "User", "Player1", "new1@email.com",
                "newUser1", LocalDate.parse("2000-03-03"));

        webTestClient.post()
                .uri(USER_BASE_URL +"/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRegisterDto)
                .exchange()
                .expectStatus().isEqualTo(CONFLICT)
                .expectBody(String.class)
                .value(errorMessage -> {
                    assert errorMessage.contains("User with nickname Player1 already exists");
                });
    }

    @Test
    void registerUser_InvalidsParams_UserNotRegistered() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("New", "User", "NewUser1", "new1@email",
                "newUser1", LocalDate.parse("2000-03-03"));

        webTestClient.post()
                .uri(USER_BASE_URL +"/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRegisterDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(errorMessage -> {
                    assert errorMessage.contains("Invalid Email: email should be in a valid format");
                });
    }

    @Test
    void registerUser_OneParamNull_UserNotRegistered() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("", "User", "NewUser1", "new1@email.com",
                "newUser1", LocalDate.parse("2000-03-03"));

        webTestClient.post()
                .uri(USER_BASE_URL +"/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRegisterDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(errorMessage -> {
                    assert errorMessage.contains("Invalid Name: name can't be empty and should have at least 3 characters and not more than 20.");
                });
    }

    @Test
    void getUserById_ValidId_UserReturned() {
        String VALID_UUID = "81099a9e-0d59-4571-a04c-31a08a711e3b";
        webTestClient.get()
                .uri(USER_BASE_URL + "/users/userById/{userId}", VALID_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void getUserById_InvalidId_BadRequestReturned() {
        String INVALID_UUID = "ce020780-1a66-4587-bec4";
        webTestClient.get()
                .uri(USER_BASE_URL + "/users/userById/{userId}", INVALID_UUID)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getUserByEmail_ValidEmail_UserReturned() {
        String VALID_EMAIL = "admin@email.com";
        webTestClient.get()
                .uri(USER_BASE_URL + "/users/userByEmail/{email}", VALID_EMAIL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void geUserByEmail_InvalidEmail_BadRequestReturned() {
        String INVALID_EMAIL = "admin@email";
        webTestClient.get()
                .uri(USER_BASE_URL + "/users/userByEmail/{email}", INVALID_EMAIL)
                .exchange()
                .expectStatus().isBadRequest();
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
                .hasSize(1);
    }

    @Test
    void getUserCrapsGameStats_CrapsGameReturned() {
        String uuidUser = "706507d4-b89f-41eb-a7eb-41838d08a08f";
        webTestClient.get()
                .uri(USER_BASE_URL + "/crapsGamesStatsByUser/{userId}?", uuidUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserCrapsGameStatsDto.class)
                .contains(new UserCrapsGameStatsDto[]{})
                .hasSize(1);
    }

}