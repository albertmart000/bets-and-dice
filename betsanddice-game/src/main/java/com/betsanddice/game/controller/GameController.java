package com.betsanddice.game.controller;

import com.betsanddice.game.dto.GameDto;
import com.betsanddice.game.service.IGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/betsanddice/api/v1/game")
public class GameController {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);

    IGameService gameService;

    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Greetings from the logger **");
        return "Hello from Game!!!";
    }

    @GetMapping("/games")
    @Operation(
            operationId = "Get all the stored games into the Database.",
            summary = "Get to see games.",
            description = "Requesting all the games through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GameDto.class), mediaType = "application/json")}),
            })
    public Flux<GameDto> getAllCrapsGames() {
        return gameService.getAllGames();
    }

}
