package authentication.services;


import authentication.dto.UserDetails;
import authentication.models.User;
import authentication.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserDetails mapUserToUserDetails(User user) {
        return new UserDetails(user.getUsername(), user.getEmail(), user.getPassword());
    }
    private User mapUserDetailsToUser(UserDetails userDetails) {
        return User.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .password(userDetails.getPassword())
                .build();
    }

    public void saveUser(UserDetails userDetails) {
        User user = mapUserDetailsToUser(userDetails);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.insertUser(user);
    }

    public UserDetails findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return mapUserToUserDetails(user);
    }

    public boolean isAuthenticUser(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }


}
