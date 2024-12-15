package esgi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import esgi.models.Book;
import esgi.models.User;
import esgi.services.BookService;
import esgi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    private UserService userService;

    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query) {
        return bookService.searchBooks(query);
    }

    @PutMapping("/update")
    public String updateBook( @RequestBody Book book) {
        return bookService.updateBook(book);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PostMapping(path = "/create",  consumes = APPLICATION_JSON_VALUE)
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }


    private String saveImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }
        try {
            String uploadDir = "../uploads/";
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
            return fileName; // Retourne le chemin ou nom du fichier
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }


}
