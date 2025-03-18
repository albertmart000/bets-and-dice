package com.betsanddice.user.controller;

import com.betsanddice.user.annotations.ValidGenericPattern;
import com.betsanddice.user.annotations.ValidUUID;
import com.betsanddice.user.dto.GenericResultDto;
import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.service.IUserService;
import com.betsanddice.user.service.client.ICrapsGameClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(
        name = "REST APIs for betsanddice-user microservice in bets-and-dice",
        description = "REST API in bets-and-dice to for user management"
)
@Validated
@RestController
@RequestMapping(value = "/betsanddice/api/v1/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String DEFAULT_OFFSET = "0";
    private static final String DEFAULT_LIMIT = "3";
    private static final String LIMIT = "^([1-9]\\d?|1\\d{2}|200)$";  // Integer in range [1, 200]
    private static final String INVALID_PARAM = "Invalid parameter";

    IUserService userService;
    ICrapsGameClientService crapsGameClient;

    public UserController(IUserService userService, ICrapsGameClientService crapsGameClient) {
        this.userService = userService;
        this.crapsGameClient = crapsGameClient;
    }

    @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Greetings from the logger **");
        return "Hello from User!!!";
    }

    @GetMapping(path = "/users/{userId}")
    @Operation(
            operationId = "Get the information from a chosen user.",
            summary = "Get to see the User Data.",
            description = "Sending the ID User through the URI to retrieve it from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "HTTP Status OK", content = {@Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "The User with given Id was not found.", content = {@Content(schema = @Schema())}),
                    @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error")
            }
    )
    public Mono<ResponseEntity<UserDto>> getOneUser(@PathVariable("userId") @ValidUUID(message = "Invalid UUID for user") String id) {
        return userService.getUserById(id)
                .map(ResponseEntity.ok()::body);
    }

    @GetMapping("/users")
    @Operation(
            operationId = "Get only the users on a page.",
            summary = "Get to see users on a page.",
            description = "Requesting the users for a page sending page number and the number of items per page through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "400", description = "Missing or unexpected parameters")
            }
    )
    public Mono<GenericResultDto<UserDto>> getAllUsers(@RequestParam(defaultValue = DEFAULT_OFFSET) @ValidGenericPattern(message = INVALID_PARAM) String offset,
                                                       @RequestParam(defaultValue = DEFAULT_LIMIT) @ValidGenericPattern(pattern = LIMIT, message = INVALID_PARAM) String limit) {
        return userService.getAllUsers((Integer.parseInt(offset)), Integer.parseInt(limit));
    }

    @GetMapping("/users/crapsGamesStatsByUser/{userId}")
    @Operation(
            operationId = "Get crapsGames statistics from a given user.",
            summary = "Get crapsGames statistics from a user.",
            description = "Retrieve crapsGames statistics for a user through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserCrapsGameStatsDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "400", description = "Missing or unexpected parameters")
            }
    )
    public Mono<ResponseEntity<UserCrapsGameStatsDto>> getUserCrapsGameStats(@PathVariable("userId") String id) {
        return crapsGameClient.getUserCrapsGameStats(id)
                .map(ResponseEntity.ok()::body);
    }

}