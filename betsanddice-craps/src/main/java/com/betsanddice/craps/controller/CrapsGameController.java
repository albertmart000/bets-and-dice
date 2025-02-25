package com.betsanddice.craps.controller;

import com.betsanddice.craps.dto.BetDto;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.GenericResultDto;
import com.betsanddice.craps.service.ICrapsGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(
        name = "REST APIs for betsanddice-craps microservice",
        description = "REST API in bets-and-dice for Craps game"
)
@RestController
@RequestMapping(value = "/betsanddice/api/v1/craps")
public class CrapsGameController {

    private static final Logger log = LoggerFactory.getLogger(CrapsGameController.class);

    private static final String DEFAULT_OFFSET = "0";
    private static final String DEFAULT_LIMIT = "3";

    ICrapsGameService crapsGameService;

    public CrapsGameController(ICrapsGameService crapsGameService) {
        this.crapsGameService = crapsGameService;
    }

    @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Greetings from the logger **");
        return "Hello from Craps!!!";
    }

    @PostMapping("/crapsGames/playAndBet/{userId}")
    @Operation(
            operationId = "Allows the chosen user to play and bet a game of craps game",
            summary = "Play one game of craps games.",
            description = "The chosen user plays and bets a game of craps that is stored in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CrapsGameDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "The User with given Id was not found.", content = {@Content(schema = @Schema())})
            })

    public Mono<ResponseEntity<CrapsGameDto>> playAndBetCrapsGameByUser(@PathVariable("userId") String userId,
                                                                        @RequestBody BetDto betDto) {
        return crapsGameService.playAndBetCrapsGameByUser(userId, betDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/crapsGames/crapsGamesByUser/{userId}")
    @Operation(
            operationId = "Get Craps Game from a user on a page.",
            summary = "Get to see crapsGame from a user on a page.",
            description = "Requesting crapsGames for a user sending page number and the number of items per page through the URI from the database. Requesting the users for a page sending page number and the number of items per page through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CrapsGameDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "400", description = "Missing or unexpected parameters")
            }
    )
    public Mono<GenericResultDto<CrapsGameDto>> getCrapsGameByUser(@PathVariable("userId") String userId,
                                                            @RequestParam(defaultValue = DEFAULT_OFFSET)  String offset,
                                                            @RequestParam(defaultValue = DEFAULT_LIMIT)  String limit) {
        return crapsGameService.getCrapsGameByUser(userId, (Integer.parseInt(offset)), Integer.parseInt(limit));
    }

}