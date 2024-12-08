package com.betsanddice.craps.controller;

import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.service.ICrapsGameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.when;

@WebFluxTest(CrapsGameController.class)
class CrapsControllerTest {

    private final String CRAPS_BASE_URL = "/betsanddice/api/v1/craps";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICrapsGameService crapsService;

    CrapsGameDto crapsGameDto = new CrapsGameDto();

    @Test
    void testHello() {
        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from Craps!!!");
    }

    @Test
    void AddCrapsGameToUserTest() {
        UUID userId = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        when(crapsService.playCrapsGameByUser(String.valueOf(userId)))
                .thenReturn(Mono.just(crapsGameDto));

        webTestClient.post()
                .uri(CRAPS_BASE_URL + "/crapsGames/{userid}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(crapsGameDto), CrapsGameDto.class)
                .exchange()
                .expectStatus().isOk()
                .equals(Mono.just(crapsGameDto));
    }
}