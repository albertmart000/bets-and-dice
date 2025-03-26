package com.betsanddice.user.service.client;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import com.betsanddice.user.exception.UserNotFoundException;
import com.betsanddice.user.repository.UserRepository;
import com.betsanddice.user.utils.StringToUuidValidator;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
//import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

class CrapsGameClientServiceImpTest {

    @Mock
    private static MockWebServer mockWebServer;

    @Mock
    private static UserRepository userRepository;

    @Mock
    private static StringToUuidValidator uuidValidator;

    @InjectMocks
    private static CrapsGameClientServiceImp crapsGameClientServiceToTest;

    private final String validId = "706507d4-b89f-41eb-a7eb-41838d08a08f";
    private final UUID userUuid = UUID.fromString(validId);
    UserDocument userDocument = new UserDocument();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockWebServer = new MockWebServer();
        WebClient mockedWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        crapsGameClientServiceToTest = new CrapsGameClientServiceImp(userRepository,
                uuidValidator, mockedWebClient);
    }

    @AfterAll
    static void tearDownServer() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void getUserCrapsGameStats_UserCrapsGameStatsReturned() {
        when(uuidValidator.validateUuid(validId)).thenReturn(Mono.just(userUuid));
        when(userRepository.findById(userUuid)).thenReturn(Mono.just(userDocument));




        mockWebServer.enqueue(new MockResponse()
                .setBody(getMockedResponse())
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Mono<UserCrapsGameStatsDto> result = crapsGameClientServiceToTest.getUserCrapsGameStats(validId);

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getUserCrapsGameStats_ShouldReturnError_WhenUserNotFound() {
        String nonExistId = "4f8a6c91-8a9d-49b0-9f2c-3e67d2b18b7d";
        UUID nonExistUuid = UUID.fromString(nonExistId);

        when(uuidValidator.validateUuid(nonExistId)).thenReturn(Mono.just(nonExistUuid));
        when(userRepository.findByUuid(nonExistUuid)).thenReturn(Mono.empty());

        Mono<UserCrapsGameStatsDto> result = crapsGameClientServiceToTest.getUserCrapsGameStats(nonExistId);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof UserNotFoundException &&
                                error.getMessage().equals("User with id " + nonExistId + " not found.")
                );
    }

    @Test
    void getUserCrapsGameStats_ShouldReturnError_WhenExternalServiceFails() {
        when(uuidValidator.validateUuid(validId)).thenReturn(Mono.just(userUuid));
        when(userRepository.findById(userUuid)).thenReturn(Mono.just(userDocument));

        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        StepVerifier.create(crapsGameClientServiceToTest.getUserCrapsGameStats(validId))
                .expectErrorMatches(Exception.class::isInstance)
                .verify();
    }

    //@NotNull
    private static String getMockedResponse() {
        return "{\"id_user\":\"706507d4-b89f-41eb-a7eb-41838d08a08f\",\"name_game\":\"Craps\",\"games_played\":2,\"games_won\":1,\"percent_games_won\":50.0,\"total_amount_bet\":20.0,\"profit_obtained\":1.5}";
    }
}