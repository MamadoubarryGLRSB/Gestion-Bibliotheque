package esgi.controllers;

import esgi.models.User;
import esgi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path= "/create", consumes = APPLICATION_JSON_VALUE)
    public String createUser(@RequestBody User user){

        return this.userService.createUser(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail()
        ));
    }

    @GetMapping("/profile-test")
    public ResponseEntity<?> getProfileTest() {
        return ResponseEntity.ok(Map.of(
                "id", 101,
                "name", "Utilisateur Test",
                "email", "test@example.com"
        ));
    }

    @PostMapping(path = "/connect", consumes = APPLICATION_JSON_VALUE)
    public String connectUser(@RequestBody User user){
        return this.userService.connectUser(user.getEmail(), user.getPassword());
    }

    @PutMapping(path = "/profil")
    public String updateProfile(@RequestBody User user){
        return this.userService.updateProfile(user);
    }

    @GetMapping(path = "/allUser")
    public List<User> allUser(){
        return this.userService.getAllUsers();
    }

    @DeleteMapping(path = "/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id){
        return this.userService.deleteUser(id);
    }
}
