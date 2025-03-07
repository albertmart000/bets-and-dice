package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.BetDto;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.GenericResultDto;
import reactor.core.publisher.Mono;

public interface ICrapsGameService {

    Mono<CrapsGameDto> playAndBetCrapsGameByUser(String userId, BetDto betDto);

    Mono<GenericResultDto<CrapsGameDto>> getCrapsGameByUser(String userId, int offset, int limit);
}