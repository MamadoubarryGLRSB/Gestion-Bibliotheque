package esgi;

import esgi.models.User;
import esgi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseTester implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Testing database connection...");
        List<User> users = userService.getAllUsers();
        System.out.println("Users found in the database:");
        users.forEach(user -> System.out.println(user.getName() + " - " + user.getEmail()));
    }
}
