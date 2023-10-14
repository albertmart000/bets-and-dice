package com.betsanddice.user.service;

import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImp implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserDto> getUserByUuid(String uuid) {
        return null;
    }
}




