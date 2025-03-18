package com.betsanddice.user.service.client;

import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CrapsGameClientServiceImp implements ICrapsGameClientService {

    private final String CRAPS_BASE_URL = "/betsanddice/api/v1/craps";

    private final WebClient webClient;

    public CrapsGameClientServiceImp(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<UserCrapsGameStatsDto> getUserCrapsGameStats(String userId) {
        return webClient.get()
                .uri(CRAPS_BASE_URL + "/crapsGames/crapsGamesStatsByUser/{userid}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserCrapsGameStatsDto.class);
    }

}