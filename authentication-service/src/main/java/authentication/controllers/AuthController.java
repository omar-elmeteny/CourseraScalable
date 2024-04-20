package authentication.controllers;


import authentication.dto.AuthRequestDTO;
import authentication.dto.JwtResponseDTO;
import authentication.dto.RegisterRequestDto;
import authentication.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        return ResponseEntity.ok(authenticationService.login(authRequestDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@RequestBody RegisterRequestDto user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping("/secure")
    public ResponseEntity<String> secret() {
        System.out.println("Post Secure");
        return ResponseEntity.ok("Post Secure");
    }

    @GetMapping("/secure")
    public ResponseEntity<String> getSecret() {
        System.out.println("Get Secure");
        return ResponseEntity.ok("Get Secure");
    }
}
