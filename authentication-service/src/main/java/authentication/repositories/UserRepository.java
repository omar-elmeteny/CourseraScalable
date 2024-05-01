package authentication.repositories;

import java.util.Optional;

import authentication.models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Cacheable(value = "users", key = "#username")
    Optional<User> findByUsername(String username);
}