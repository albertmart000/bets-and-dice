package com.betsanddice.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator betsanddiceRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/betsanddice/craps/**")
                        .filters( f -> f.rewritePath("/betsanddice/craps/(?<segment>.*)","/${segment}"))
                        .uri("lb://BETSANDDICE-CRAPS"))
                .route(p -> p
                        .path("/betsanddice/user/**")
                        .filters( f -> f.rewritePath("/betsanddice/user/(?<segment>.*)","/${segment}"))
                        .uri("lb://BETSANDDICE-USER")).build();
    }
}
