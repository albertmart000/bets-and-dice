package com.betsanddice.auth.service;

import com.betsanddice.auth.dto.User;
import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<User> fetchUserData(String email);
}
