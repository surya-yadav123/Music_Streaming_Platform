package in.main.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import in.main.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA automatically implements this method at runtime
    Optional<User> findByEmail(String email);
}
