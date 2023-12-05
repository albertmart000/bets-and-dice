package com.betsanddice.craps.helper;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.DiceRollDto;
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

class CrapsGameDocumentToDtoConverterTest {

    private CrapsGameDocumentToDtoConverter converter;

    private CrapsGameDocument crapsGameDocument1;
    private CrapsGameDocument crapsGameDocument2;

    private CrapsGameDto crapsGameDto1;
    private CrapsGameDto crapsGameDto2;

    @BeforeEach
    public void setup() {
        converter = new CrapsGameDocumentToDtoConverter();

        UUID uuidCrapsGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID uuidCrapsGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        DiceRollDto diceRollDto1= new DiceRollDto( 1, 2, 3);
        DiceRollDto diceRollDto2= new DiceRollDto( 3, 4, 7);
        List<DiceRollDto> diceRollsList = List.of(diceRollDto1, diceRollDto2);

        LocalDateTime date = LocalDateTime.of(2023, 1, 31, 12, 0, 0);

        crapsGameDocument1 = new CrapsGameDocument(uuidCrapsGame1, uuidUser, date, 2, diceRollsList);
        crapsGameDocument2 = new CrapsGameDocument(uuidCrapsGame2, uuidUser, date, 2, diceRollsList);

        crapsGameDto1 = getCrapsGameDtoMocked(uuidCrapsGame1, uuidUser,"2023-01-31 12:00:00", diceRollsList, 2);
        crapsGameDto2 = getCrapsGameDtoMocked(uuidCrapsGame2, uuidUser,"2023-01-31 12:00:00", diceRollsList, 2);
    }

    @Test
    @DisplayName("Conversion from CrapsGameDocument to CrapsGameDto. Testing 'fromDocumentToDto' method.")
    void testConvertFromDocumentToDto() {
        CrapsGameDocument crapsGameDocumentMocked = crapsGameDocument1;
        CrapsGameDto resultDto = converter.fromDocumentToDto(crapsGameDocumentMocked);
        CrapsGameDto expectedDto = crapsGameDto1;

        assertThat(expectedDto).usingRecursiveComparison()
                .isEqualTo(resultDto);
    }

    @Test
    @DisplayName("Testing Flux conversion. Test fromDocumentFluxToDtoFlux method.")
    void fromFluxDocToFluxDto() {

        CrapsGameDocument crapsGameDocument1 = this.crapsGameDocument1;
        CrapsGameDocument crapsGameDocument2 = this.crapsGameDocument2;

        Flux<CrapsGameDto> resultDto = converter.fromDocumentFluxToDtoFlux(Flux.just(crapsGameDocument1, crapsGameDocument2));

        CrapsGameDto expectedDto1 = crapsGameDto1;
        CrapsGameDto expectedDto2 = crapsGameDto2;

        assertThat(resultDto.count().block()).isEqualTo(Long.valueOf(2));
        assertThat(resultDto.blockFirst()).usingRecursiveComparison()
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .isEqualTo(expectedDto2);
    }

    private CrapsGameDto getCrapsGameDtoMocked(UUID uuidCrapsGame, UUID uuidUser, String date,
                                               List<DiceRollDto> diceRollsDtoList, Integer attempts) {
        CrapsGameDto crapsGameDtoMocked = mock(CrapsGameDto.class);
        when(crapsGameDtoMocked.getUuid()).thenReturn(uuidCrapsGame);
        when(crapsGameDtoMocked.getUserId()).thenReturn(uuidUser);
        when(crapsGameDtoMocked.getDate()).thenReturn(date);
        when(crapsGameDtoMocked.getDiceRollsList()).thenReturn(diceRollsDtoList);
        when(crapsGameDtoMocked.getAttempts()).thenReturn(attempts);
        return crapsGameDtoMocked;
    }

}
