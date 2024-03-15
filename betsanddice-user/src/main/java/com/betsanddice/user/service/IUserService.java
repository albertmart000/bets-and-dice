package com.betsanddice.user.service;

import com.betsanddice.user.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Flux<UserDto> getAllUsers(int offset, int limit);

    Mono<UserDto> getUserByUuid(String uuid);
}
