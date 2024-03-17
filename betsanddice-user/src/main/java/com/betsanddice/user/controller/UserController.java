package com.betsanddice.user.controller;

import com.betsanddice.user.annotations.ValidGenericPattern;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping(value = "/betsanddice/api/v1/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String DEFAULT_OFFSET = "0";
    private static final String DEFAULT_LIMIT = "200";  //if no limit, all elements (avoid exception with default value 200)
    private static final String LIMIT = "^([1-9]\\d?|1\\d{2}|200)$";  // Integer in range [1, 200]
    private static final String INVALID_PARAM = "Invalid parameter";
    private static final String NO_SERVICE = "No Services";

    IUserService userService;

    private final DiscoveryClient discoveryClient;

    public UserController(IUserService userService, DiscoveryClient discoveryClient) {
        this.userService = userService;
        this.discoveryClient = discoveryClient;
    }

    @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Greetings from the logger **");

        Optional<String> userService = discoveryClient.getInstances("betsanddice-user")
                .stream()
                .findAny()
                .map(Object::toString);

        Optional<String> crapsService = discoveryClient.getInstances("betsanddice-craps")
                .stream()
                .findAny()
                .map(Object::toString);

        Optional<String> tutorialService = discoveryClient.getInstances("betsanddice-tutorial")
                .stream()
                .findAny()
                .map(Object::toString);

        log.info("~~~~~~~~~~~~~~~~~~~~~~");
        log.info("Scanning micros:");
        log.info((userService.orElse(NO_SERVICE))
                .concat(System.lineSeparator())
                .concat(crapsService.orElse(NO_SERVICE))
                .concat(System.lineSeparator())
                .concat(tutorialService.orElse(NO_SERVICE)));
        log.info("~~~~~~~~~~~~~~~~~~~~~~");

        return "Hello from Bets And Dice!!!";
    }

    @GetMapping(path = "/users/{userId}")
    @Operation(
            operationId = "Get the information from a chosen user.",
            summary = "Get to see the User Data.",
            description = "Sending the ID User through the URI to retrieve it from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "The User with given Id was not found.", content = {@Content(schema = @Schema())})
            }
    )
    public Mono<UserDto> getOneUser(@PathVariable("userId") String id) {
        return userService.getUserByUuid(id);
    }

    @GetMapping("/users")
    @Operation(
            operationId = "Get only the users on a page.",
            summary = "Get to see users on a page.",
            description = "Requesting the users for a page sending page number and the number of items per page through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "No users were found.", content = {@Content(schema = @Schema())})
            })
    public Flux<UserDto> getAllUsers(@RequestParam(defaultValue = DEFAULT_OFFSET) @ValidGenericPattern(message = INVALID_PARAM) String offset,
                                     @RequestParam(defaultValue = DEFAULT_LIMIT) @ValidGenericPattern(pattern = LIMIT, message = INVALID_PARAM) String limit) {
        return userService.getAllUsers((Integer.parseInt(offset)), Integer.parseInt(limit));
    }

}
