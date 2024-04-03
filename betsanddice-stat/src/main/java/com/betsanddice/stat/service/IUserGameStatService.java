package com.betsanddice.stat.service;

import com.betsanddice.stat.dto.UserGameStatDto;
import reactor.core.publisher.Flux;

public interface IUserGameStatService {

    Flux<UserGameStatDto> getAllUserStats();

}
