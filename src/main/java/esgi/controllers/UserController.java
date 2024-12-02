package esgi.controllers;

import esgi.models.User;
import esgi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path= "/register", consumes = APPLICATION_JSON_VALUE)
    public String creerUser(@RequestBody User user){

        System.out.println("Creating user: " + user);

        return this.userService.creerUser(user);
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
