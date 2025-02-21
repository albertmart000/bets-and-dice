package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.BetDto;
import com.betsanddice.craps.dto.CrapsGameDto;
import reactor.core.publisher.Mono;

public interface ICrapsGameService {

    Mono<CrapsGameDto> playAndBetCrapsGameByUser(String userId, BetDto betDto);

}