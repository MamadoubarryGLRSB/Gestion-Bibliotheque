package esgi.Factory;

import esgi.models.Role;
import esgi.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactoryImpl implements UserFactory{
    @Override
    public User createUser(String name, String email, String password, Role role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}
