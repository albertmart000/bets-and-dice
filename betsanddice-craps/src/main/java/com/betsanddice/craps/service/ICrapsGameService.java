package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.BetDto;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.GenericResultDto;
import com.betsanddice.craps.dto.UserCrapsGameStatsDto;
import reactor.core.publisher.Mono;

public interface ICrapsGameService {

    Mono<CrapsGameDto> playAndBetCrapsGameByUser(String userId, BetDto betDto);

    Mono<GenericResultDto<CrapsGameDto>> getCrapsGameByUser(String userId, int offset, int limit);

    Mono<UserCrapsGameStatsDto> getUserCrapsGameStats(String userId);
}