package com.betsanddice.craps.controller;

import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.service.ICrapsGameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(CrapsGameController.class)
class CrapsGameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICrapsGameService crapsGameService;

    @MockBean
    private DiscoveryClient discoveryClient;

    @Test
    void testHello() {
        webTestClient.get()
                .uri("/betsanddice/api/v1/craps/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello from Craps!!!");
    }

    @Test
    void getAllCrapsGames_CrapsGamesExist_CrapsGamesReturned() {
        CrapsGameDto crapsGameDto1 = new CrapsGameDto();
        CrapsGameDto crapsGameDto2 = new CrapsGameDto();

        CrapsGameDto[] expectedCrapsGames = {crapsGameDto1, crapsGameDto2};
        Flux<CrapsGameDto> expectedCrapsGamesFlux = Flux.just(expectedCrapsGames);

        when(crapsGameService.getAllCrapsGame()).thenReturn(expectedCrapsGamesFlux);

        webTestClient.get()
                .uri("/betsanddice/api/v1/craps/crapsGames")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class);
    }

}