package esgi.services;

import esgi.Factory.UserFactory;
import esgi.models.Role;
import esgi.models.User;
import esgi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private  final UserFactory userFactory;

    public UserService(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    public String createUser(User user){

        User existingUser = this.userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            // L'email existe déjà, donc on peut lancer une exception
          return ("Email is already in use.");
        }
        User users = userFactory.createUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());

        this.userRepository.save(users);
        return "User created successfully";
    }

    public String connectUser(String email, String password){
        List<User> listUser= this.userRepository.findAll();

        // Parcourir la liste et vérifier si un utilisateur avec cet email et mot de passe existe
        for (User user : listUser) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)){
                return "User connect : "+ user.getEmail();
            }
        }

        return "Incorrect email address or password.";

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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
