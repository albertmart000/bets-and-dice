package com.betsanddice.game.controller;

import com.betsanddice.game.dto.CrapsGameDto;
import com.betsanddice.game.dto.DiceRollDto;
import com.betsanddice.game.service.ICrapsGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebFluxTest(CrapsGameController.class)
class CrapsGameControllerTest {

    private final String CRAPS_BASE_URL = "/betsanddice/api/v1/craps";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICrapsGameService crapsGameService;

    @MockBean
    private DiscoveryClient discoveryClient;

    UUID userId = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

    CrapsGameDto crapsGameDto1 = new CrapsGameDto();
    CrapsGameDto crapsGameDto2 = new CrapsGameDto();
    CrapsGameDto[] expectedCrapsGames = {crapsGameDto1, crapsGameDto2};

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID uuidCrapsGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID uuidCrapsGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        DiceRollDto diceRollDto1= new DiceRollDto( 1, 2, 3);
        DiceRollDto diceRollDto2= new DiceRollDto( 3, 4, 7);
        List<DiceRollDto> diceRollsDtoList = List.of(diceRollDto1, diceRollDto2);

        crapsGameDto1 = new CrapsGameDto(uuidCrapsGame1, userId, LocalDateTime.now().toString(),
                2, diceRollsDtoList);
        crapsGameDto2 = new CrapsGameDto(uuidCrapsGame2, userId, LocalDateTime.now().toString(),
                2, diceRollsDtoList);
    }

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
        Flux<CrapsGameDto> expectedCrapsGamesFlux = Flux.just(expectedCrapsGames);

        when(crapsGameService.getAllCrapsGame()).thenReturn(expectedCrapsGamesFlux);

        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class);
    }

    @Test
    void AddCrapsGameToUserTest(){

        when(crapsGameService.addCrapsGameToUser(String.valueOf(userId))).thenAnswer(x->(Mono.just(crapsGameDto1)));

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