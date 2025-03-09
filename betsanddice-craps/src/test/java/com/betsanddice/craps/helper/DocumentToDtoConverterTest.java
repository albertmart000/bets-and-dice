package com.betsanddice.craps.helper;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.ResultCrapsGameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentToDtoConverterTest {

    private DocumentToDtoConverter<CrapsGameDocument, CrapsGameDto> converter;

    private CrapsGameDocument crapsGameDocument1;
    private CrapsGameDocument crapsGameDocument2;

    private CrapsGameDto crapsGameDto1;
    private CrapsGameDto crapsGameDto2;

    @BeforeEach
    public void setup() {
        converter = new DocumentToDtoConverter<>();

        UUID crapsGameUuid1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID crapsGameUuid2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        UUID userUuid = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
        LocalDateTime date = LocalDateTime.of(2023, 1, 31, 12, 0, 0);
        int expectedDiceSum = 7;
        int expectedAttempts = 2;
        double amountBet = 10.0;

        ResultCrapsGameDto resultBetDto  = new ResultCrapsGameDto(5, true, 2.0, 20.0);

        DiceRollDocument diceRollDocument1= new DiceRollDocument( 1, 2);
        DiceRollDocument diceRollDocument2= new DiceRollDocument( 3, 4);
        List<DiceRollDocument> diceRollsList = List.of(diceRollDocument1, diceRollDocument2);

        crapsGameDocument1 = new CrapsGameDocument(crapsGameUuid1, userUuid, date, expectedDiceSum, expectedAttempts,
                amountBet, diceRollsList);
        crapsGameDocument2 = new CrapsGameDocument(crapsGameUuid2, userUuid, date, expectedDiceSum, expectedAttempts,
                amountBet, diceRollsList);

        crapsGameDto1 = getCrapsGameDtoMocked(crapsGameUuid1, userUuid,"2023-01-31 12:00:00", expectedDiceSum, expectedAttempts,
                amountBet, diceRollsList, resultBetDto);
        crapsGameDto2 = getCrapsGameDtoMocked(crapsGameUuid2, userUuid,"2023-01-31 12:00:00", expectedDiceSum, expectedAttempts,
                amountBet, diceRollsList, resultBetDto);
    }

    @Test
    @DisplayName("Conversion from CrapsGameDocument to CrapsGameDto. Testing 'fromDocumentToDto' method.")
    void testConvertFromDocumentToDto() {
        CrapsGameDocument crapsGameDocumentMocked = crapsGameDocument1;
        CrapsGameDto resultDto = converter.fromDocumentToDto(crapsGameDocumentMocked, CrapsGameDto.class);
        CrapsGameDto expectedDto = crapsGameDto1;

        assertThat(expectedDto).usingRecursiveComparison()
                .ignoringFields("result")
                .isEqualTo(resultDto);
    }
    @Test
    @DisplayName("Testing Flux conversion. Test fromDocumentFluxToDtoFlux method.")
    void fromFluxDocToFluxDto() {
        Flux<CrapsGameDto> resultDto = converter.fromDocumentFluxToDtoFlux(Flux.just(crapsGameDocument1, crapsGameDocument2), CrapsGameDto.class);

        CrapsGameDto expectedDto1 = crapsGameDto1;
        CrapsGameDto expectedDto2 = crapsGameDto2;

        assertThat(resultDto.count().block()).isEqualTo(Long.valueOf(2));
        assertThat(resultDto.blockFirst()).usingRecursiveComparison()
                .ignoringFields("result")
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .ignoringFields("result")
                .isEqualTo(expectedDto2);
    }

    private CrapsGameDto getCrapsGameDtoMocked (UUID uuid, UUID userUuid, String date, int expectedDiceSum, int expectedAttempts,
                                                double amountBet, List<DiceRollDocument> diceRollsList, ResultCrapsGameDto resultDto) {
        CrapsGameDto crapsGameDtoMocked = mock(CrapsGameDto.class);
        when(crapsGameDtoMocked.getUuid()).thenReturn(uuid);
        when(crapsGameDtoMocked.getUserId()).thenReturn(userUuid);
        when(crapsGameDtoMocked.getDate()).thenReturn(date);
        when(crapsGameDtoMocked.getExpectedDiceSum()).thenReturn(expectedDiceSum);
        when(crapsGameDtoMocked.getExpectedAttempts()).thenReturn(expectedAttempts);
        when(crapsGameDtoMocked.getAmountBet()).thenReturn(amountBet);
        when(crapsGameDtoMocked.getDiceRollsList()).thenReturn(diceRollsList);
        when(crapsGameDtoMocked.getResult()).thenReturn(resultDto);
        return crapsGameDtoMocked;
    }
}