package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.CrapsGameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CrapsServiceImpTest {

    @InjectMocks
    private CrapsServiceImp crapsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCrapsGameToUser() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";
        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        Mono<CrapsGameDto> result = crapsService.addCrapsGameToUser(userId);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    assertNotNull(dto);
                    assertEquals(uuidUser, dto.getUserId());
                    return true;
                })
                .verifyComplete();
    }
}