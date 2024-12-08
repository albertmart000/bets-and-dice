package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.DiceRollDto;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.helper.DocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CrapsServiceImpTest {

    @Mock
    private CrapsGameRepository crapsGameRepository;

    @Mock
    private DocumentToDtoConverter<CrapsGameDocument, CrapsGameDto> converter;

    @InjectMocks
    private CrapsGameServiceImp crapsGameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlayCrapsGameByUserValidUuid() {
        String uuid = "123e4567-e89b-12d3-a456-426655440000";
        CrapsGameDocument crapsGameDocument = CrapsGameDocument.builder()
                .uuid(UUID.randomUUID())
                .userId(UUID.fromString(uuid))
                .date(LocalDateTime.now())
                .diceRollsList(List.of(new DiceRollDto(1, 2, 3)))
                .build();
        CrapsGameDto crapsGameDto = CrapsGameDto.builder()
                .uuid(crapsGameDocument.getUuid())
                .userId(crapsGameDocument.getUserId())
                .date(String.valueOf(crapsGameDocument.getDate()))
                .diceRollsList(crapsGameDocument.getDiceRollsList())
                .build();

        when(crapsGameRepository.save(any(CrapsGameDocument.class))).thenReturn(Mono.just(crapsGameDocument));
        when(converter.fromDocumentToDto(any(CrapsGameDocument.class), any(Class.class))).thenReturn(crapsGameDto);

        Mono<CrapsGameDto> result = crapsGameService.playCrapsGameByUser(uuid);

        StepVerifier.create(result)
                .assertNext(dto -> {
                    assertThat(dto.getUuid()).isEqualTo(crapsGameDocument.getUuid());
                    assertThat(dto.getUserId()).isEqualTo(crapsGameDocument.getUserId());
                    assertThat(dto.getDate()).isEqualTo(String.valueOf(crapsGameDocument.getDate()));
                    assertThat(dto.getDiceRollsList()).isEqualTo(crapsGameDocument.getDiceRollsList());
                })
                .verifyComplete();
    }

    @Test
    void testPlayCrapsGameByUserInvalidUuid() {
        String uuid = "invalid-uuid";

        Mono<CrapsGameDto> result = crapsGameService.playCrapsGameByUser(uuid);

        StepVerifier.create(result)
                .expectError(BadUuidException.class)
                .verify();
    }
}