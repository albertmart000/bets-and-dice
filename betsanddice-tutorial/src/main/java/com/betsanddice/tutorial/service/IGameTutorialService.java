package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.dto.in.GameTutorialDtoByName;
import com.betsanddice.tutorial.dto.out.GameTutorialDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGameTutorialService {

    Mono<GameTutorialDto> addGameTutorial(GameTutorialDtoByName gameTutorialDtoByName);

    Flux<GameTutorialDto> getAllGameTutorials();

    Mono<GameTutorialDto> getGameTutorialByUuid(String uuid);
}
