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
        logger.info("Login request received from: {}", authRequestDTO.getUsername());
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
                logger.info("User authenticated: {}", result.getUsername());
                return
                        ResponseEntity.ok().body(
                                jwtService.generateTokens(result.getUsername(), null)
                        );
            } else {
                logger.error("User authentication failed: {}", result.getUsername());
                return ResponseEntity.badRequest().body(result.getMessage());
            }

        } catch (MessageQueueException e) {
            logger.error("Command {} failed", CommandNames.LOGIN_COMMAND);
            return commandError(CommandNames.LOGIN_COMMAND, e);
        }
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody RegistrationDTO registrationDTO) {
        logger.info("Register request received from: {}", registrationDTO.getUsername());
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
                logger.info("User registered: {}", registrationDTO.getUsername());
                return ResponseEntity
                        .ok()
                        .body(null);

            } else {
                logger.error("User registration failed: {}", registrationDTO.getUsername());
                return ResponseEntity
                        .badRequest()
                        .body(result.getValidationMessages());
            }

        } catch (MessageQueueException e) {
            logger.error("Command {} failed", CommandNames.REGISTER_COMMAND);
            return commandError(CommandNames.REGISTER_COMMAND, e);
        }
    }

    @PostMapping("refresh-token")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody JwtResponseDTO jwtResponseDTO) {
        logger.info("Refresh token request received");
        try {
            JwtResponseDTO tokens = jwtService.refreshTokens(jwtResponseDTO.getRefreshToken());
            if (tokens == null) {
                logger.error("Refresh token invalid");
                return ResponseEntity.badRequest().build();
            }
            logger.info("Tokens refreshed");
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
        logger.info("Change password request received from: {}", userDetails.getUsername());
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
                logger.info("Password changed successfully: {}", userDetails.getUsername());
                return ResponseEntity.ok().body("Password changed successfully");
            } else {
                logger.error("Password change failed: {}", userDetails.getUsername());
                return ResponseEntity.badRequest().body(result.getValidationError());
            }

        } catch (MessageQueueException e) {
            logger.error("Command {} failed", CommandNames.CHANGE_PASSWORD_COMMAND);
            return commandError(CommandNames.CHANGE_PASSWORD_COMMAND, e);
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetUserPasswordRequest request) {
        logger.info("Reset password request received from: {}", request.getEmail());
        try {
            ChangePasswordResult result = this.commandDispatcher.sendCommand(
                    CommandNames.RESET_USER_PASSWORD,
                    request,
                    ChangePasswordResult.class
            );

            if (result.isSuccessful()) {
                logger.info("Password reset successfully: {}", request.getEmail());
                return ResponseEntity.ok().body("Password reset successfully");
            } else {
                logger.error("Password reset failed: {}", request.getEmail());
                return ResponseEntity.badRequest().body(result.getValidationError());
            }

        } catch (MessageQueueException e) {
            logger.error("Command {} failed", CommandNames.RESET_USER_PASSWORD);
            return commandError(CommandNames.RESET_USER_PASSWORD, e);
        }
    }

    @PostMapping("verify-email")
    public ResponseEntity<Object> verifyEmail(@RequestBody VerificationRequest request) {
        logger.info("Verify email request received from: {}", request.getEmail());
        try {
            VerificationResult result = this.commandDispatcher.sendCommand(
                    CommandNames.VERIFY_EMAIL_COMMAND,
                    VerificationRequest.builder()
                            .email(request.getEmail())
                            .otp(request.getOtp())
                            .build(),
                    VerificationResult.class
            );

            if (result.isSuccessful()) {
                logger.info("Email verified successfully: {}", request.getEmail());
                return ResponseEntity.ok().body("Email verified successfully");
            } else {
                logger.error("Email verification failed: {}", request.getEmail());
                return ResponseEntity.badRequest().body(result.getErrorMessages());
            }

        } catch (MessageQueueException e) {
            logger.error("Command {} failed", CommandNames.VERIFY_EMAIL_COMMAND);
            return commandError(CommandNames.VERIFY_EMAIL_COMMAND, e);
        }
    }

    @PostMapping("forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        logger.info("Forgot password request received from: {}", request.getEmail());
        try {
            ForgotPasswordResult result = this.commandDispatcher.sendCommand(
                    CommandNames.FORGOT_PASSWORD,
                    request,
                    ForgotPasswordResult.class
            );

            if (result.isSuccessful()) {
                logger.info("Forgot password request processed successfully: {}", request.getEmail());
                return ResponseEntity.ok().body(result.getMessage());
            } else {
                logger.error("Forgot password request failed: {}", request.getEmail());
                return ResponseEntity.badRequest().body(result.getMessage());
            }

        } catch (MessageQueueException e) {
            logger.error("Command {} failed", CommandNames.FORGOT_PASSWORD);
            return commandError(CommandNames.FORGOT_PASSWORD, e);
        }
    }
}
