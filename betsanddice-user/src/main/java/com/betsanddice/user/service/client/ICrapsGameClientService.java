package com.betsanddice.user.service.client;

import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import reactor.core.publisher.Mono;

public interface ICrapsGameClientService {

    Mono<UserCrapsGameStatsDto> getUserCrapsGameStats(String userId);

}
