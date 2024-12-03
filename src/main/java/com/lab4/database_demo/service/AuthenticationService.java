package com.lab4.database_demo.service;

import com.lab4.database_demo.controller.dtos.JWTResponse;
import com.lab4.database_demo.controller.dtos.LogInRequest;
import com.lab4.database_demo.controller.dtos.SignUpRequest;

public interface AuthenticationService {

    public JWTResponse signup(SignUpRequest request) throws Exception;

    public JWTResponse login(LogInRequest request);
}
