package com.lab4.database_demo.controller;

import com.lab4.database_demo.controller.dtos.JWTResponse;
import com.lab4.database_demo.controller.dtos.LogInRequest;
import com.lab4.database_demo.controller.dtos.SignUpRequest;
import com.lab4.database_demo.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JWTResponse> signup(@RequestBody SignUpRequest request){
        try {
            return  ResponseEntity.ok(authenticationService.signup(request));
        }catch (Exception e){
            JWTResponse error = new JWTResponse();
            error.setError(e.getMessage());
            return ResponseEntity.status(400).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> signing(@RequestBody LogInRequest request){
        try {
            return ResponseEntity.ok(authenticationService.login(request));
        }catch (Exception e){
            log.info(e.getMessage());
            JWTResponse error = new JWTResponse();
            error.setError(e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
