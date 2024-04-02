package com.betsanddice.stat.service;

import com.betsanddice.stat.dto.UserGameStatDto;
import reactor.core.publisher.Flux;

public interface IUserStatService {

    Flux<UserGameStatDto> getAllUserStats();

}
