package com.betsanddice.tutorial.controller;

import com.betsanddice.tutorial.annotations.ValidGenericPattern;
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
    private static final String DEFAULT_OFFSET = "0";
    private static final String DEFAULT_LIMIT = "200";  //if no limit, all elements (avoid exception with default value 200)
    private static final String LIMIT = "^([1-9]\\d?|1\\d{2}|200)$";  // Integer in range [1, 200]
    private static final String INVALID_PARAM = "Invalid parameter";

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
            operationId = "Get the information from a chosen Tutorial Id.",
            summary = "Get to see the Tutorial Data.",
            description = "Sending the Id Tutorial  through the URI to retrieve it from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GameTutorialDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "The Tutorial with given Id was not found.", content = {@Content(schema = @Schema())})
            }
    )
    public Mono<GameTutorialDto> getGameTutorialByUuid(@PathVariable("gameTutorialUuid") String uuid) {
        return gameTutorialService.getGameTutorialByUuid(uuid);
    }

    @GetMapping(path = "/gameTutorials/game/{gameUuid}")
    @Operation(
            operationId = "Get the Tutorial from a chosen Game.",
            summary = "Get to see the Game Tutorial.",
            description = "Sending the Id Game through the URI to retrieve it from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GameTutorialDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "The Game with given Id was not found.", content = {@Content(schema = @Schema())})
            }
    )
    public Mono<GameTutorialDto> getGameTutorialByGameUuid(@PathVariable("gameUuid") String uuid) {
        return gameTutorialService.getGameTutorialByGameId(uuid);
    }

    @GetMapping("/gameTutorials")
    @Operation(
            operationId = "Get only the game tutorials on a page.",
            summary = "Get to see games tutorials.",
            description = "Requesting the games tutorials for a page sending page number and the number of items per page through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GameTutorialDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "No games were found.", content = {@Content(schema = @Schema())})
            })
    public Flux<GameTutorialDto> getAllGameTutorials(@RequestParam(defaultValue = DEFAULT_OFFSET) @ValidGenericPattern(message = INVALID_PARAM) String offset,
                                                     @RequestParam(defaultValue = DEFAULT_LIMIT) @ValidGenericPattern(pattern = LIMIT, message = INVALID_PARAM) String limit) {
        return gameTutorialService.getAllGameTutorials((Integer.parseInt(offset)), Integer.parseInt(limit));
    }

}
