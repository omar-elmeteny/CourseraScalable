package com.guctechie.usermanagementweb.controllers;



import com.guctechie.messagequeue.exceptions.MessageQueueException;
import com.guctechie.messagequeue.services.CommandDispatcher;
import com.guctechie.usermanagementmodels.AuthRequestDTO;
import com.guctechie.usermanagementmodels.CommandNames;
import com.guctechie.usermanagementmodels.JwtResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final CommandDispatcher commandDispatcher;

    public AuthController(
            CommandDispatcher commandDispatcher
    ) {

        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping("login")
    public JwtResponseDTO login(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            return this.commandDispatcher.sendCommand(
                    CommandNames.LOGIN_COMMAND,
                    authRequestDTO,
                    JwtResponseDTO.class
            );
        } catch (MessageQueueException e) {
            throw new RuntimeException(e);
        }

    }

//    @PostMapping("register")
//    public JwtResponseDTO register(@RequestBody UserDetails userDetails) {
//        userService.saveUser(userDetails);
//        return new JwtResponseDTO(jwtService.generateToken(userDetails.getUsername()));
//    }
//
//    @PostMapping("refresh-token")
//    public JwtResponseDTO refreshToken(@RequestBody JwtResponseDTO jwtResponseDTO) {
//        return new JwtResponseDTO(jwtService.refreshToken(jwtResponseDTO.getAccessToken()));
//    }

}
