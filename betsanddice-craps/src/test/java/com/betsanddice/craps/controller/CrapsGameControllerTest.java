package com.betsanddice.craps.controller;

import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.*;
import com.betsanddice.craps.exception.CrapsGameNotFoundException;
import com.betsanddice.craps.service.ICrapsGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(CrapsGameController.class)
class CrapsGameControllerTest {

    private final String CRAPS_BASE_URL = "/betsanddice/api/v1/craps";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ICrapsGameService crapsGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void playAndBetCrapsGameByUserTest() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";
        BetDto betDto = new BetDto(7, 5, 10);
        int expectedDiceSum = 7;
        int expectedAttempts = 2;
        double amountBet = 10.0;

        DiceRollDocument diceRollDocument1 = new DiceRollDocument(1, 2);
        DiceRollDocument diceRollDocument2 = new DiceRollDocument(3, 4);
        List<DiceRollDocument> diceRollsDocumentList = List.of(diceRollDocument1, diceRollDocument2);

        ResultCrapsGameDto resultDto = new ResultCrapsGameDto(2, true, 2.0, 20.0);

        CrapsGameDto crapsGameDto = new CrapsGameDto(UUID.randomUUID(), UUID.fromString(userId),
                "2023-01-31 12:46:29", expectedDiceSum, expectedAttempts, amountBet,
                diceRollsDocumentList, resultDto);

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

    @Test
    void getCrapsGamesByUser_ValidPageParameters_CrapsGameReturned() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        GenericResultDto<CrapsGameDto> expectedResult = new GenericResultDto<>();
        expectedResult.setInfo(0, 2, 2, new CrapsGameDto[]{new CrapsGameDto(), new CrapsGameDto()});

        Mono<GenericResultDto<CrapsGameDto>> expectedResultMono = Mono.just(expectedResult);

        String offset = "0";
        String limit = "2";

        when(crapsGameService.getCrapsGameByUser(userId, Integer.parseInt(offset), Integer.parseInt(limit)))
                .thenReturn(expectedResultMono);

        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesByUser/{userid}offset=0&limit=2", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class);
    }

    @Test
    void getCrapsGamesByUser_NullPageParameters_CrapsGameReturned() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        GenericResultDto<CrapsGameDto> expectedResult = new GenericResultDto<>();
        expectedResult.setInfo(0, 3, 3, new CrapsGameDto[]{new CrapsGameDto(), new CrapsGameDto(),
                new CrapsGameDto()});

        Mono<GenericResultDto<CrapsGameDto>> expectedResultMono = Mono.just(expectedResult);

        String offsetDefaultValue = "0";
        String limitDefaultValue = "3";

        when(crapsGameService.getCrapsGameByUser(userId, Integer.parseInt(offsetDefaultValue),
                Integer.parseInt(limitDefaultValue))).thenReturn(expectedResultMono);

        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesByUser/{userid}", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class);
    }

    @Test
    void getCrapsGamesByUser_CrapsGameNotFound_ThrowsException() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        String offsetDefaultValue = "0";
        String limitDefaultValue = "3";

        when(crapsGameService.getCrapsGameByUser(userId, Integer.parseInt(offsetDefaultValue),
                Integer.parseInt(limitDefaultValue)))
                .thenThrow(new CrapsGameNotFoundException("No CrapsGames found for User with id " + userId));

        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesByUser/{userid}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrapsGameDto.class);
    }

    @Test
    void getUserCrapsGameStats_CrapsGameStatsReturned() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        UserCrapsGameStatsDto expectedUserCrapsGameStatsDto = new UserCrapsGameStatsDto();

        when(crapsGameService.getUserCrapsGameStats(userId))
                .thenReturn(Mono.just(expectedUserCrapsGameStatsDto));

        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesStatsByUser/{userid}", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserCrapsGameStatsDto.class);
    }

    @Test
    void getUserCrapsGameStats_CrapsGameStatsNotFound_ThrowsException() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        when(crapsGameService.getUserCrapsGameStats(userId))
                .thenThrow(new CrapsGameNotFoundException("No CrapsGames found for User with id " + userId));

        webTestClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesStatsByUser/{userid}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserCrapsGameStatsDto.class);
    }

    @Test
    void deleteCrapsGamesByUserId_CrapsGameDeleted() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";
        DeleteResponseDto deleteResponseDto = new DeleteResponseDto(userId, "CrapsGames deleted successfully.");
        Mono<DeleteResponseDto> response = Mono.just(deleteResponseDto);

        when(crapsGameService.deleteCrapsGamesByUserId(userId)).thenReturn(response);

        Mono<DeleteResponseDto> result = crapsGameService.deleteCrapsGamesByUserId(userId);

        StepVerifier.create(result)
                .expectNext(deleteResponseDto)
                .verifyComplete();
    }
}