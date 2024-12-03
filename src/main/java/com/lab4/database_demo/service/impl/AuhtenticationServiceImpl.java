package com.lab4.database_demo.service.impl;

import com.lab4.database_demo.controller.dtos.JWTResponse;
import com.lab4.database_demo.controller.dtos.LogInRequest;
import com.lab4.database_demo.controller.dtos.SignUpRequest;
import com.lab4.database_demo.entity.UserEntity;
import com.lab4.database_demo.repository.UserRepository;
import com.lab4.database_demo.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuhtenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JWTResponse signup(SignUpRequest request) throws Exception{

        var user = UserEntity.builder().name(request.getName()).mail(request.getMail()).
                password(passwordEncoder.encode(request.getPassword())).role("ROLE_CLIENT").build();
        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return JWTResponse.builder().token(jwt).build();
    }

    public JWTResponse login(LogInRequest request) {
        log.debug("Attempting login for email: {}", request.getMail());


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword()));
            log.debug("Authentication successful for email: {}", request.getMail());
        } catch (Exception e) {
            log.error("Authentication failed for email: {}", request.getMail(), e);
            throw new BadCredentialsException("Invalid email or password");
        }


        var user = userRepository.findByMail(request.getMail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        log.debug("User retrieved: {}", user.getMail());


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Password mismatch for user: {}", request.getMail());
            throw new BadCredentialsException("Invalid email or password");
        }
        log.debug("Password verified for user: {}", request.getMail());


        var jwt = jwtService.generateToken(user);
        log.debug("JWT generated for user: {}", request.getMail());


        return JWTResponse.builder().token(jwt).build();
    }

}
