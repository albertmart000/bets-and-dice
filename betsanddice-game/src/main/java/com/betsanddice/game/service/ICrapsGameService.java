package com.betsanddice.game.service;

import com.betsanddice.game.dto.CrapsGameDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICrapsGameService {

    Flux<CrapsGameDto> getAllCrapsGame();
    Mono<CrapsGameDto> addCrapsGameToUser(String userId);

}
