package com.lab4.database_demo.controller;

import com.lab4.database_demo.controller.dtos.JWTResponse;
import com.lab4.database_demo.controller.dtos.LogInRequest;
import com.lab4.database_demo.controller.dtos.SignUpRequest;
import com.lab4.database_demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user account")
    @RequestBody(description = "Sign-up details", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignUpRequest.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponse.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<JWTResponse> signup(@RequestBody SignUpRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.signup(request));
        } catch (Exception e) {
            JWTResponse error = new JWTResponse();
            error.setError(e.getMessage());
            return ResponseEntity.status(400).body(error);
        }
    }

    @Operation(summary = "Log in an existing user")
    @RequestBody(description = "Log-in credentials", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LogInRequest.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<JWTResponse> signing(@RequestBody LogInRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.login(request));
        } catch (Exception e) {
            log.info(e.getMessage());
            JWTResponse error = new JWTResponse();
            error.setError(e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
