package com.betsanddice.tutorial.controller;

import com.betsanddice.tutorial.dto.GameTutorialDto;
import com.betsanddice.tutorial.service.IGameTutorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/betsanddice/api/v1/tutorial")
public class GameTutorialController {

    private static final Logger log = LoggerFactory.getLogger(GameTutorialController.class);

    IGameTutorialService gameTutorialService;

    public GameTutorialController(IGameTutorialService gameTutorialService) {
        this.gameTutorialService = gameTutorialService;
    }

    @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Gretings from the logger **");
        return "Hello from Tutorial!!!";
    }

    @PostMapping("/gameTutorials")
    @Operation(
            operationId = "Allows to add a new game tutorial in the database",
            summary = "Add game tutorial.",
            description = "A new game tutorial is stored in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GameTutorialDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema())}),
                    @ApiResponse(responseCode = "503", description = "Service Unavailable", content = {@Content(schema = @Schema())})
            })
    public Mono<GameTutorialDto> addGameTutorial(@Valid @RequestBody GameTutorialDto gameTutorialDto) {
        return gameTutorialService.addGameTutorial(gameTutorialDto);
    }

    @GetMapping(path = "/gameTutorials/{gameTutorialUuid}")
    @Operation(
            operationId = "Get the information from a chosen Game.",
            summary = "Get to see the Game Data.",
            description = "Sending the Id Game through the URI to retrieve it from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GameTutorialDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "The Game with given Id was not found.", content = {@Content(schema = @Schema())})
            }
    )
    public Mono<GameTutorialDto> getGameTutorialByUuid(@PathVariable("gameTutorialUuid") String uuid) {
        return gameTutorialService.getGameTutorialByUuid(uuid);
    }

    @GetMapping("/gameTutorials")
    @Operation(
            operationId = "Get all the stored games into the Database.",
            summary = "Get to see games.",
            description = "Requesting all the games through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GameTutorialDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "No games were found.", content = {@Content(schema = @Schema())})
            })
    public Flux<GameTutorialDto> getAllGameTutorials() {
        return gameTutorialService.getAllGameTutorials();
    }

}
