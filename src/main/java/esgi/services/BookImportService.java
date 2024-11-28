package esgi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esgi.models.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookImportService {

    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public BookImportService(BookService bookService) {
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    public void importBooks(String isbn) {
        String apiUrl = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Appel API
            String response = restTemplate.getForObject(apiUrl, String.class);

            // Traitement des données
            String title = extractTitleFromJson(response, isbn);
            String author = extractAuthorFromJson(response, isbn);
            String image = extractImageFromJson(response, isbn);

            // Insérer dans la base de données
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setImage(image);
            book.setAvailability(true);
            bookService.createBook(book);

        } catch (Exception e) {
            System.out.println("Erreur lors de l'importation des livres : " + e.getMessage());
        }
    }

    private String extractTitleFromJson(String json, String isbn) throws Exception {
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode bookNode = rootNode.path("ISBN:" + isbn);
        return bookNode.path("title").asText(null); // Retourne null si le champ "title" n'existe pas
    }

    private String extractAuthorFromJson(String json, String isbn) throws Exception {
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode bookNode = rootNode.path("ISBN:" + isbn);
        JsonNode authorsNode = bookNode.path("authors");

        if (authorsNode.isArray() && authorsNode.size() > 0) {
            return authorsNode.get(0).path("name").asText(null); // Récupère le nom du premier auteur
        }
        return null;
    }

    private String extractImageFromJson(String json, String isbn) throws Exception {
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode bookNode = rootNode.path("ISBN:" + isbn);
        return bookNode.path("cover").path("medium").asText(null); // URL de l'image (medium size)
    }
}
