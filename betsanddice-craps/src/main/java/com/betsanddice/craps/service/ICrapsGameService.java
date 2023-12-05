package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.CrapsGameDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICrapsGameService {

    Flux<CrapsGameDto> getAllCrapsGame();
    Mono<CrapsGameDto> addCrapsGameToUser(String userId);

}
