package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.CrapsGameDto;
import reactor.core.publisher.Mono;

public interface ICrapsGameService {

    Mono<CrapsGameDto> playCrapsGameByUser(String userId);

    Mono<CrapsGameDto> playAndBetCrapsGameByUser(String userId, double amountWagered, int myResult);

}
