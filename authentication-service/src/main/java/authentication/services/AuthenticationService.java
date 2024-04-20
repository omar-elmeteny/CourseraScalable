package authentication.services;

import authentication.dto.AuthRequestDTO;
import authentication.dto.JwtResponseDTO;
import authentication.dto.RegisterRequestDto;
import authentication.models.User;
import authentication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public JwtResponseDTO register(RegisterRequestDto register){
        var user = User.builder()
                .username(register.getUsername())
                .password(passwordEncoder.encode(register.getPassword()))
                .email(register.getEmail())
                .firstName(register.getFirstName())
                .lastName(register.getLastName())
                .role(register.getRole())
                .build();
        var savedUser = userRepository.save(user);
        return JwtResponseDTO.builder()
                .accessToken(jwtService.generateToken(savedUser.getUsername()))
                .build();
    }

    public JwtResponseDTO login(AuthRequestDTO request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var password = request.getPassword();
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        return JwtResponseDTO.builder()
                .accessToken(jwtService.generateToken(user.getUsername()))
                .build();
    }

}
