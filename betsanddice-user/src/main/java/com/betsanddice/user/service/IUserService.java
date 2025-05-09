package com.betsanddice.user.service;

import com.betsanddice.user.dto.GenericResultDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.dto.UserRegisterDto;
import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<UserDto> registerUser (UserRegisterDto userRegisterDto);
    Mono<UserDto> getUserById(String id);
    Mono<GenericResultDto<UserDto>> getAllUsers(int offset, int limit);

}