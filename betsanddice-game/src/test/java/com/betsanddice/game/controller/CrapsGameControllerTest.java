package com.betsanddice.game.controller;

import com.betsanddice.game.dto.CrapsGameDto;
import com.betsanddice.game.service.ICrapsGameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.when;

@WebFluxTest(CrapsGameController.class)
class CrapsGameControllerTest {

    private final String CRAPS_BASE_URL = "/betsanddice/api/v1/game/craps";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICrapsGameService crapsGameService;

    CrapsGameDto crapsGameDto1 = new CrapsGameDto();
    CrapsGameDto crapsGameDto2 = new CrapsGameDto();

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
    void getAllCrapsGames_CrapsGamesExist_CrapsGamesReturned() {
        CrapsGameDto[] expectedCrapsGames = {crapsGameDto1, crapsGameDto2};
        Flux<CrapsGameDto> expectedCrapsGamesFlux = Flux.just(expectedCrapsGames);

        when(crapsGameService.getAllCrapsGame()).thenReturn(expectedCrapsGamesFlux);

        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class);
    }

    @Test
    void AddCrapsGameToUserTest() {
        UUID userId = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        when(crapsGameService.addCrapsGameToUser(String.valueOf(userId))).thenAnswer(x -> (Mono.just(crapsGameDto1)));

        webTestClient.post()
                .uri(CRAPS_BASE_URL + "/crapsGames/{userid}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(crapsGameDto1), CrapsGameDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
        //.equals(Mono.just(crapsGameDto1));
    }

}