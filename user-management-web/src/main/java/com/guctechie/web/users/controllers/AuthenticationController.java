package com.guctechie.web.users.controllers;

import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.services.CommandDispatcher;
import com.guctechie.messages.CommandNames;
import com.guctechie.users.models.*;
import com.guctechie.web.users.dtos.AuthenticationRequestDTO;
import com.guctechie.web.users.dtos.JwtResponseDTO;
import com.guctechie.web.users.dtos.RegistrationDTO;
import com.guctechie.web.users.services.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final CommandDispatcher commandDispatcher;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(
            CommandDispatcher commandDispatcher,
            JwtService jwtService
    ) {

        this.commandDispatcher = commandDispatcher;
        this.jwtService = jwtService;
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequestDTO authRequestDTO) {
        try {
            AuthenticationResult result = this.commandDispatcher.sendCommand(
                    CommandNames.LOGIN_COMMAND,
                    new AuthenticationRequest(authRequestDTO.getUsername(), authRequestDTO.getPassword()),
                    AuthenticationResult.class
            );

            if (result.isAuthenticated()) {
                return
                        ResponseEntity.ok().body(
                            new JwtResponseDTO(jwtService.generateToken(result.getUsername()))
                        );
            } else {
                return ResponseEntity.badRequest().body("Invalid username or password");
            }

        } catch (MessageQueueException e) {
            logger.error("Error while handling login command", e);
            // Don't expose internal server error to the client
            throw new RuntimeException("Authentication failed due to internal server error");
        }
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody RegistrationDTO registrationDTO) {
        try {
            RegistrationResult result = this.commandDispatcher.sendCommand(
                    CommandNames.REGISTER_COMMAND,
                    RegistrationRequest.builder()
                            .username(registrationDTO.getUsername())
                            .email(registrationDTO.getEmail())
                            .password(registrationDTO.getPassword())
                            .fullName(registrationDTO.getFullName())
                            .dateOfBirth(registrationDTO.getDateOfBirth())
                            .profilePhotoUrl(registrationDTO.getProfilePhotoUrl())
                            .phoneNumber(registrationDTO.getPhoneNumber())
                            .build()
                    ,
                    RegistrationResult.class
            );

            if (result.isSuccessful()) {
                return ResponseEntity
                        .ok()
                        .body(JwtResponseDTO.builder()
                                .accessToken(jwtService.generateToken(result.getUsername())
                        ).build());

            } else {
                return ResponseEntity
                        .badRequest()
                        .body(result.getValidationMessages());
            }

        } catch (MessageQueueException e) {
            logger.error("Error while handling registration command", e);
            // Don't expose internal server error to the client
            throw new RuntimeException("Authentication failed due to internal server error");
        }
    }

    @PostMapping("refresh-token")
    public JwtResponseDTO refreshToken(@RequestBody JwtResponseDTO jwtResponseDTO) {
        return new JwtResponseDTO(jwtService.refreshToken(jwtResponseDTO.getAccessToken()));
    }
}
