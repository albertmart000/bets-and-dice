package com.betsanddice.user.service.client;

import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import com.betsanddice.user.exception.UserNotFoundException;
import com.betsanddice.user.repository.UserRepository;
import com.betsanddice.user.utils.StringToUuidValidator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CrapsGameClientServiceImp implements ICrapsGameClientService {

    private final String CRAPS_BASE_URL = "/craps";

    private final UserRepository userRepository;
    private final StringToUuidValidator uuidValidator;
    private final WebClient webClient;

    public CrapsGameClientServiceImp(UserRepository userRepository, StringToUuidValidator uuidValidator, WebClient webClient) {
        this.userRepository = userRepository;
        this.uuidValidator = uuidValidator;
        this.webClient = webClient;
    }

    @Override
    public Mono<UserCrapsGameStatsDto> getUserCrapsGameStats(String id) {
        return uuidValidator.validateUuid(id)
                .flatMap(userId -> userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + userId + " not found")))
                .flatMap(user -> webClient.get()
                        .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesStatsByUser/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(UserCrapsGameStatsDto.class)
                ));
    }
}