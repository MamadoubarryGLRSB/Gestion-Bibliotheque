package esgi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestThymeleaf {

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
