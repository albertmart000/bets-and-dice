package com.betsanddice.auth.service;

import com.betsanddice.auth.dto.User;
import com.betsanddice.auth.exception.CustomBadRequestException;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
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

class UserServiceImplTest {

    @Mock
    private static MockWebServer mockWebServer;

    @InjectMocks
    private static UserServiceImpl userServiceToTest;

    private final String email = "test@example.com";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockWebServer = new MockWebServer();
        WebClient mockedWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        userServiceToTest = new UserServiceImpl(mockedWebClient);
    }

    @AfterAll
    static void tearDownServer() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void fetchUserData_UserDataReturned() {
        mockWebServer.enqueue(new MockResponse()
                .setBody(getMockedResponse())
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Mono<User> result = userServiceToTest.fetchUserData(email);

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void fetchUserData_ShouldThrowCustomBadRequestException_When4xxErrorOccurs() {
        String errorBody = "Invalid email format";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(errorBody)
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        StepVerifier.create(userServiceToTest.fetchUserData(email))
                .expectErrorMatches(throwable -> throwable instanceof CustomBadRequestException &&
                        throwable.getMessage().equals("400 - " + errorBody))
                .verify();
    }

    @Test
    void fetchUserData_ShouldReturnError_WhenExternalServiceFails() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        StepVerifier.create(userServiceToTest.fetchUserData(email))
                .expectErrorMatches(Exception.class::isInstance)
                .verify();
    }

    private static String getMockedResponse() {
        return "{\"id_user\":\"706507d4-b89f-41eb-a7eb-41838d08a08f\",\"email\":\"test@example.com\",\"password\":\"password\"}";
    }
}