package com.betsanddice.craps.controller;

import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.service.ICrapsGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/betsanddice/api/v1/craps")
public class CrapsGameController {

    private static final Logger log = LoggerFactory.getLogger(CrapsGameController.class);

    @Autowired
    ICrapsGameService crapsGameService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Greetings from the logger **");
        return "Hello from Craps!!!";
    }

    @PostMapping("/{userId}/games")
    @Operation(
            operationId = "Allows the chosen user to play a game of craps game",
            summary = "Play one game of craps games.",
            description = "The chosen user plays a game of craps that is stored in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CrapsGameDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "The User with given Id was not found.", content = {@Content(schema = @Schema())})
            })
    public Mono<CrapsGameDto> addCrapsGameToUser(String userId) {
        return crapsGameService.addCrapsGame(userId);
    }

    @GetMapping("/crapsGames")
    @Operation(
            operationId = "Get all the stored craps games into the Database.",
            summary = "Get to see craps games.",
            description = "Requesting all the craps games through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CrapsGameDto.class), mediaType = "application/json")}),
            })
    public Flux<CrapsGameDto> getAllCrapsGames() {
        return crapsGameService.getAllCrapsGame();
    }

}