package com.betsanddice.game.service;

import com.betsanddice.game.dto.GameDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGameService {

    Mono<GameDto> getGameByUuid(String uuid);

    Flux<GameDto> getAllGames();
}
