package com.betsanddice.user.service.client;

import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import reactor.core.publisher.Mono;

public interface ICrapsClientService {

    Mono<UserCrapsGameStatsDto> getUserCrapsGameStats(String userId);

}
