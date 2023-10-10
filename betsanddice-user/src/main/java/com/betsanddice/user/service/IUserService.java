package com.betsanddice.user.service;

import com.betsanddice.user.dto.UserDto;
import reactor.core.publisher.Flux;

public interface IUserService {
     Flux<UserDto> getAllUsers();
}
