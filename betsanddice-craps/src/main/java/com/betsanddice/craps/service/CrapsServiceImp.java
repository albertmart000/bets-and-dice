package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.DiceRollDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
public class CrapsServiceImp implements ICrapsService {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Mono<CrapsGameDto> addCrapsGameToUser(String userId) {
        return generateDiceRollsList()
                .map(diceRollsList -> new CrapsGameDto(UUID.fromString(userId),
                        diceRollsList.size(), diceRollsList));
    }

    private Mono<List<DiceRollDto>> generateDiceRollsList() {
        return Flux.<DiceRollDto>generate(flux -> {
                    int dice1 = secureRandom.nextInt(6) + 1;
                    int dice2 = secureRandom.nextInt(6) + 1;
                    int result = dice1 + dice2;
                    flux.next(new DiceRollDto(dice1, dice2, result));
                    if (result == 7) {
                        flux.complete();
                    }
                })
                .collectList();
    }
}