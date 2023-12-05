package com.betsanddice.stat.controller;

import com.betsanddice.stat.dto.UserStatDto;
import com.betsanddice.stat.service.IUserStatService;
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
@RequestMapping(value = "/betsanddice/api/v1/stat")
public class UserStatController {

    private static final Logger log = LoggerFactory.getLogger(UserStatController.class);

    private final IUserStatService userStatService;

    public UserStatController(IUserStatService userStatService) {
        this.userStatService = userStatService;
    }

    @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Greetings from the logger **");
        return "Hello from Stat!!!";
    }

    @GetMapping("/userStats")
    @Operation(
            operationId = "Get all the stored users stats into the Database.",
            summary = "Get to see users stats",
            description = "Requesting all the users stats through the URI from the database.",

            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserStatDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "No games were found.", content = {@Content(schema = @Schema())})
            })
    public Flux<UserStatDto> getAllUserStats() {
        return userStatService.getAllUserStats();
    }

}


