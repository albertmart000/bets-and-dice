package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.dto.GameTutorialDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGameTutorialService {

    Flux<GameTutorialDto> getAllGameTutorials();

    Mono<GameTutorialDto> getGameTutorialByUuid(String uuid);
}
