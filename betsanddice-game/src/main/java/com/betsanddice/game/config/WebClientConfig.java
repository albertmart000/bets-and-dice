package com.betsanddice.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {

    private static final String TUTORIAL_BASE_URL = "http://localhost:8098//betsanddice/api/v1/tutorial";
    //private static final String TUTORIAL_BASE_URL = "http://localhost:8098//betsanddice/api/v1/tutorial/";

    @Bean
    @Description("Tutorial web client")
    public WebClient tutorialClient (String serviceBaseUrl) {
        return WebClient.builder()
                .baseUrl(TUTORIAL_BASE_URL)
                .build();
    }
}
