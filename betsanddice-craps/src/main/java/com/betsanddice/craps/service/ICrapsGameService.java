package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.*;
import reactor.core.publisher.Mono;

public interface ICrapsGameService {

    Mono<CrapsGameDto> playAndBetCrapsGameByUser(String userId, BetDto betDto);

    Mono<GenericResultDto<CrapsGameDto>> getCrapsGameByUser(String userId, int offset, int limit);

    Mono<UserCrapsGameStatsDto> getUserCrapsGameStats(String userId);

    Mono<DeleteResponseDto> deleteCrapsGamesByUserId(String id);

}