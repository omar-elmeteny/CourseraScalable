package com.guctechie.web.users.controllers;

import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.services.CommandDispatcher;
import com.guctechie.messages.CommandNames;
import com.guctechie.users.models.*;
import com.guctechie.web.users.dtos.AuthenticationRequestDTO;
import com.guctechie.web.users.dtos.ChangePasswordDTO;
import com.guctechie.web.users.dtos.JwtResponseDTO;
import com.guctechie.web.users.dtos.RegistrationDTO;
import com.guctechie.web.users.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController extends BaseController {

    private final CommandDispatcher commandDispatcher;
    private final JwtService jwtService;

    public AuthenticationController(
            CommandDispatcher commandDispatcher,
            JwtService jwtService
    ) {

        this.commandDispatcher = commandDispatcher;
        this.jwtService = jwtService;
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(
            @RequestBody AuthenticationRequestDTO authRequestDTO,
            HttpServletRequest request
    ) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        try {
            AuthenticationResult result = this.commandDispatcher.sendCommand(
                    CommandNames.LOGIN_COMMAND,
                    AuthenticationRequest.builder()
                            .username(authRequestDTO.getUsername())
                            .password(authRequestDTO.getPassword())
                            .ipAddress(ipAddress)
                            .userAgent(request.getHeader("User-Agent"))
                            .build(),
                    AuthenticationResult.class
            );

            if (result.isAuthenticated()) {
                return
                        ResponseEntity.ok().body(
                                jwtService.generateTokens(result.getUsername(), null)
                        );
            } else {
                return ResponseEntity.badRequest().body(result.getMessage());
            }

        } catch (MessageQueueException e) {
            return commandError(CommandNames.LOGIN_COMMAND, e);
        }
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody RegistrationDTO registrationDTO) {
        try {
            ArrayList<String> roles = new ArrayList<>();
            roles.add("student");
            RegistrationResult result = this.commandDispatcher.sendCommand(
                    CommandNames.REGISTER_COMMAND,
                    RegistrationRequest.builder()
                            .username(registrationDTO.getUsername())
                            .email(registrationDTO.getEmail())
                            .password(registrationDTO.getPassword())
                            .firstName(registrationDTO.getFirstName())
                            .lastName(registrationDTO.getLastName())
                            .dateOfBirth(registrationDTO.getDateOfBirth())
                            .profilePhotoUrl(registrationDTO.getProfilePhotoUrl())
                            .phoneNumber(registrationDTO.getPhoneNumber())
                            .roles(roles)
                            .build()
                    ,
                    RegistrationResult.class
            );

            if (result.isSuccessful()) {
                return ResponseEntity
                        .ok()
                        .body(null);

            } else {
                return ResponseEntity
                        .badRequest()
                        .body(result.getValidationMessages());
            }

        } catch (MessageQueueException e) {
            return commandError(CommandNames.REGISTER_COMMAND, e);
        }
    }

    @PostMapping("refresh-token")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody JwtResponseDTO jwtResponseDTO) {
        try {
            JwtResponseDTO tokens = jwtService.refreshTokens(jwtResponseDTO.getRefreshToken());
            if (tokens == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(tokens);
        } catch (MessageQueueException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("change-password")
    public ResponseEntity<Object> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            ChangePasswordResult result = this.commandDispatcher.sendCommand(
                    CommandNames.CHANGE_PASSWORD_COMMAND,
                    ChangePasswordRequest.builder()
                            .username(userDetails.getUsername())
                            .oldPassword(changePasswordDTO.getOldPassword())
                            .newPassword(changePasswordDTO.getNewPassword())
                            .build()
                    ,
                    ChangePasswordResult.class
            );

            if (result.isSuccessful()) {
                return ResponseEntity.ok().body("Password changed successfully");
            } else {
                return ResponseEntity.badRequest().body(result.getValidationError());
            }

        } catch (MessageQueueException e) {
            return commandError(CommandNames.CHANGE_PASSWORD_COMMAND, e);
        }
    }
}
