package esgi.repositories;

import esgi.models.Role;
import esgi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    List<User> findByRole(Role role);
}
