package com.betsanddice.stat.service;

import com.betsanddice.stat.dto.UserStatDto;
import reactor.core.publisher.Flux;

public interface IUserStatService {

    Flux<UserStatDto> getAllUserStats();

}
