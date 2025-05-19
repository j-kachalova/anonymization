package com.kachalova.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Маршрут для RuleSet Service
                .route("ruleset_route", r -> r.path("/api/rulesets/**")
                        .uri("http://ruleset:8082"))
                // Маршрут для Job Service
                .route("job_route", r -> r.path("/api/jobs/**")
                        .uri("http://job-service:8083"))
                // Маршрут для File Processing Service (файлы)
                .route("file_route", r -> r.path("/files/**")
                        .uri("http://file-processing:8084"))
                // Маршрут для File Processing Service (анонимизация)
                .route("anonymization_route", r -> r.path("/api/anonymization/**")
                        .uri("http://file-processing:8084"))
                .build();
    }
}
