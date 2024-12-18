package esgi.services;

import esgi.Factory.UserFactory;
import esgi.models.Role;
import esgi.models.User;
import esgi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private  final UserFactory userFactory;

    public UserService(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    public String createUser(User user) {

        // Vérification si l'email existe déjà
        User existingUser = this.userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return "Email is already in use.";
        }

        // Définir un rôle par défaut si aucun n'est fourni
        Role role = (user.getRole() != null) ? user.getRole() : Role.MEMBER;

        // Créer l'utilisateur avec le rôle par défaut
        User users = userFactory.createUser(user.getName(), user.getEmail(), user.getPassword(), role);

        this.userRepository.save(users);
        return "User created successfully";
    }

    // Dans votre service
    public Map<String, String> connectUser(String email, String password) {
        List<User> listUser = this.userRepository.findAll();

        for (User user : listUser) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                String token = generateToken(user); // Générez un jeton d'authentification

                Map<String, String> response = new HashMap<>();
                response.put("message", "User connected successfully");
                response.put("email", user.getEmail());
                response.put("token", token); // Renvoyez le token dans la réponse
                response.put("status", "200");
                return response;
            }
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Incorrect email address or password");
        errorResponse.put("status", "401");
        return errorResponse;
    }

    private String generateToken(User user) {
        // Implémentez la logique de génération de token ici
        // Utilisez par exemple un framework comme JWT (Java Web Token)
        return "generated_token_" + user.getId();
    }


    public String updateProfile( User user){

        Optional<User> user_id = this.userRepository.findById(user.getId());

        if (user_id.isPresent()){
            User existingUser = user_id.get();

           if (user.getEmail() != null){
               existingUser.setEmail(user.getEmail());
           }

           if (user.getName() != null){
               existingUser.setName(user.getName());
           }


           if (user.getPassword() != null){
               existingUser.setPassword(user.getPassword());
           }

            this.userRepository.save(existingUser);

           return "Modification successfully completed";

        }

        else {
            return( "User not found.");
        }
    }

    public  List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public String deleteUser(Integer id){
        Optional<User> userDelete = this.userRepository.findById(id);

        if (userDelete.isPresent()){
            this.userRepository.delete(userDelete.get());
            return "User delete with sucess ";
        }

        return "User does not exist";

    }

    // Ajouter un bibliothécaire
    public User addLibrarian(User librarian) {
        if (librarian.getRole() != Role.LIBRARIAN) {
            throw new IllegalArgumentException("User role must be LIBRARIAN");
        }
        return userRepository.save(librarian);
    }

    // Obtenir la liste des bibliothécaires
    public List<User> getAllLibrarians() {
        return userRepository.findByRole(Role.LIBRARIAN);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Supprimer un bibliothécaire (par ADMIN)
    public void deleteLibrarian(Integer librarianId) {
        User librarian = userRepository.findById(librarianId)
                .orElseThrow(() -> new RuntimeException("Librarian not found"));
        if (librarian.getRole() != Role.LIBRARIAN) {
            throw new RuntimeException("User is not a librarian");
        }
        userRepository.delete(librarian);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));
    }

}
