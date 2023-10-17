package com.betsanddice.user.service;

import com.betsanddice.user.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Flux<UserDto> getAllUsers();

    Mono<UserDto> getUserByUuid(String uuid);
}
