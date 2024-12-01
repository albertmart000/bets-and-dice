package com.betsanddice.craps.service;

import com.betsanddice.craps.dto.CrapsGameDto;
import reactor.core.publisher.Mono;

public interface ICrapsService {

    Mono<CrapsGameDto> addCrapsGameToUser(String userId);

}
