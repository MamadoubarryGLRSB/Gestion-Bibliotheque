package esgi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "esgi")
public class BibliothequeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BibliothequeApplication.class, args);
    }
}
