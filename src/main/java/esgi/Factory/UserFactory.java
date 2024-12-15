package esgi.Factory;

import esgi.models.Role;
import esgi.models.User;

public interface UserFactory {
    User createUser(String name, String email, String password, Role role);
}
