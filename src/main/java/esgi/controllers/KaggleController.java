package esgi.controllers;

import esgi.services.BookImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kaggle")
public class KaggleController {

    private final BookImportService bookImportService;

    public KaggleController(BookImportService bookImportService) {
        this.bookImportService = bookImportService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importBooksFromKaggle(@RequestParam String filePath) {
        bookImportService.importBooksFromKaggle(filePath);
        return ResponseEntity.ok("Livres importés depuis Kaggle avec succès !");
    }
}
