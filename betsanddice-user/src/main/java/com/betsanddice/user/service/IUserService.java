package com.betsanddice.user.service;

import com.betsanddice.user.dto.UserDto;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<UserDto> getUserByUuid(String uuid);

}
