package com.betsanddice.craps.controller;

import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.BetDto;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.ResultDto;
import com.betsanddice.craps.service.ICrapsGameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(CrapsGameController.class)
class CrapsControllerTest {

    private final String CRAPS_BASE_URL = "/betsanddice/api/v1/craps";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICrapsGameService crapsGameService;

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
    void playAndBetCrapsGameByUserTest() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";
        BetDto betDto = new BetDto(7, 5, 10);

        DiceRollDocument diceRollDocument1 = new DiceRollDocument(1, 2);
        DiceRollDocument diceRollDocument2 = new DiceRollDocument(3, 4);
        List<DiceRollDocument> diceRollsDocumentList = List.of(diceRollDocument1, diceRollDocument2);

        ResultDto resultDto = new ResultDto(2, true, 2.0, 20.0);

        CrapsGameDto crapsGameDto = new CrapsGameDto(UUID.randomUUID(), UUID.fromString(userId),
                "2023-01-31 12:46:29", betDto, diceRollsDocumentList, resultDto);

        when(crapsGameService.playAndBetCrapsGameByUser(any(), any()))
                .thenReturn(Mono.just(crapsGameDto));

        webTestClient.post()
                .uri(CRAPS_BASE_URL + "/crapsGames/playAndBet/{userid}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(betDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CrapsGameDto.class);

        verify(crapsGameService).playAndBetCrapsGameByUser(anyString(), any(BetDto.class));
    }

}