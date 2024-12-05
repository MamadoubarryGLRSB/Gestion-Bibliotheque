package esgi.controllers;

import esgi.models.Book;
import esgi.services.BookImportService;
import esgi.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_LIMIT = 50;

    private final BookService bookService;
    private final BookImportService bookImportService;

    public BookController(BookService bookService, BookImportService bookImportService) {
        this.bookService = bookService;
        this.bookImportService = bookImportService;
    }

    /**
     * Récupère une liste paginée de livres avec métadonnées.
     *
     * @param page  Numéro de la page (par défaut {@value DEFAULT_PAGE}).
     * @param limit Nombre d'éléments par page (par défaut {@value DEFAULT_LIMIT}).
     * @return Une réponse contenant les livres et les métadonnées de pagination.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getBooksPaginated(
            @RequestParam(defaultValue = "" + DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = "" + DEFAULT_LIMIT) int limit) {
        List<Book> books = bookService.getBooksPaginated(page, limit);
        long totalBooks = bookService.countBooks();

        Map<String, Object> response = Map.of(
                "books", books,
                "currentPage", page,
                "totalBooks", totalBooks,
                "totalPages", (int) Math.ceil((double) totalBooks / limit)
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Récupère un livre par son ID.
     *
     * @param id ID du livre à récupérer.
     * @return Une réponse contenant le livre ou un statut 404 si non trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau livre.
     *
     * @param book Détails du livre à créer.
     * @return Une réponse contenant le livre créé.
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.ok(createdBook);
    }

    /**
     * Met à jour un livre existant.
     *
     * @param id          ID du livre à mettre à jour.
     * @param bookDetails Détails mis à jour du livre.
     * @return Une réponse contenant le livre mis à jour ou un statut 404 si non trouvé.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookService.updateBook(id, bookDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un livre par son ID.
     *
     * @param id ID du livre à supprimer.
     * @return Une réponse avec un statut 204 si la suppression est réussie ou 404 si non trouvé.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Importe des livres depuis un fichier CSV.
     *
     * @param filePath Chemin du fichier CSV.
     * @return Un message indiquant le succès ou l'échec de l'importation.
     */
    @PostMapping("/import")
    public ResponseEntity<String> importBooks(@RequestParam String filePath) {
        try {
            bookImportService.importBooksFromKaggle(filePath);
            return ResponseEntity.ok("Importation réussie depuis Kaggle avec le fichier : " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'importation : " + e.getMessage());
        }
    }
}
