package esgi.controllers;

import esgi.models.User;
import esgi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
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
    @PostMapping(path= "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody User user){
        String message = userService.createUser(user);
        return ResponseEntity.ok(Collections.singletonMap("message", message));
    }

    @PostMapping(path = "/connect", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> connectUser(@RequestBody User user) {
        Map<String, String> response = this.userService.connectUser(user.getEmail(), user.getPassword());

        if (response.get("status").equals("200")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
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
