package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.CrapsGameDto;
import reactor.core.publisher.Flux;

public interface ICrapsGameService {

    Flux<CrapsGameDto> getAllCrapsGame();
    CrapsGameDto addCrapsGameToUser(String userId);

}
