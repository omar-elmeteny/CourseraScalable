package authentication.controllers;


import authentication.dto.AuthRequestDTO;
import authentication.dto.JwtResponseDTO;
import authentication.dto.UserDetails;
import authentication.services.JwtService;
import authentication.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authenticate/")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("login")
    public JwtResponseDTO login(@RequestBody AuthRequestDTO authRequestDTO) {
        if (userService.isAuthenticUser(authRequestDTO.getUsername(), authRequestDTO.getPassword() )){
            return new JwtResponseDTO(jwtService.generateToken(authRequestDTO.getUsername()));
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @PostMapping("register")
    public JwtResponseDTO register(@RequestBody UserDetails userDetails) {
        userService.saveUser(userDetails);
        return new JwtResponseDTO(jwtService.generateToken(userDetails.getUsername()));
    }

    @PostMapping("refresh-token")
    public JwtResponseDTO refreshToken(@RequestBody JwtResponseDTO jwtResponseDTO) {
        return new JwtResponseDTO(jwtService.refreshToken(jwtResponseDTO.getAccessToken()));
    }

}
