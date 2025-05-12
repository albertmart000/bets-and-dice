package com.betsanddice.auth.service.client;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserClientServiceImpl implements IUserClientService {

    @Override
    public Mono<String> callUserTest() {
        return null;
    }
}
