package esgi.services;

import esgi.models.User;
import esgi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String creerUser(User user){

        User existingUser = this.userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            // L'email existe déjà, donc on peut lancer une exception
          return ("Email is already in use.");
        }

        this.userRepository.save(user);
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

        System.out.println("Voila ca : "+ user.getName());
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
}
