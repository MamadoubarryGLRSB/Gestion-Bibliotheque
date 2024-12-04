package esgi.controllers;

import esgi.models.Book;
import esgi.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    private final BookService bookService;

    // Injection de BookService via le constructeur
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Récupérer tous les livres
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Récupérer un livre par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            Optional<Book> book = bookService.getBookById(id);
            return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Ajouter un nouveau livre
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
            Book savedBook = bookService.addBook(book);
            return ResponseEntity.status(201).body(savedBook);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Mettre à jour un livre
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        try {
            Book updatedBook = bookService.updateBook(id, bookDetails);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        } catch (Exception e) {

            return ResponseEntity.status(500).build();
        }
    }

    // Filtrer les livres (par titre, auteur ou genre)
    @GetMapping("/filter")
    public ResponseEntity<List<Book>> filterBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {
        try {
            List<Book> filteredBooks = bookService.filterBooks(title, author, genre);
            return ResponseEntity.ok(filteredBooks);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
