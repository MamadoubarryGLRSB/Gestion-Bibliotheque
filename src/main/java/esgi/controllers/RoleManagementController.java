package esgi.controllers;


import esgi.models.User;
import esgi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleManagementController {

    @Autowired
    private UserService userService;

    // Ajouter un bibliothécaire
    @PostMapping("/librarians")
    public User addLibrarian(@RequestBody User librarian) {
        return userService.addLibrarian(librarian);
    }

    // Récupérer tous les bibliothécaires
    @GetMapping("/librarians")
    public List<User> getAllLibrarians() {
        return userService.getAllLibrarians();
    }

    // Supprimer un bibliothécaire
    @DeleteMapping("/librarians/{id}")
    public void deleteLibrarian(@PathVariable Integer id) {
        userService.deleteLibrarian(id);
    }
}
