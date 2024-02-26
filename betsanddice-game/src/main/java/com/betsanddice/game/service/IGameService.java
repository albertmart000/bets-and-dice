package com.betsanddice.game.service;

import com.betsanddice.game.dto.GameDto;
import reactor.core.publisher.Flux;

public interface IGameService {

    Flux<GameDto> getAllGames();

}
