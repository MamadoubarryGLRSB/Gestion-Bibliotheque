package esgi.services;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import esgi.models.Book;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;

import org.springframework.core.io.ClassPathResource;

@Service
public class BookImportService {

    private final BookService bookService;
    private final LibraryService libraryService;

    public BookImportService(BookService bookService, LibraryService libraryService) {
        this.bookService = bookService;
        this.libraryService = libraryService;
    }


    public void importBooksFromKaggle(String filePath) {
        try {
            // Charger le fichier CSV depuis le chemin spécifié
            ClassPathResource resource = new ClassPathResource("books_data/books.csv");
            File file = resource.getFile();

            // Configurer le lecteur CSV avec le séparateur ';'
            try (CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build()) // Définit le séparateur
                    .build()) {

                String[] nextLine;

                // Ignorer l'en-tête
                reader.readNext();

                // Lire les lignes
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine.length < 8) { // Vérifie que toutes les colonnes nécessaires sont présentes
                        System.err.println("Ligne ignorée : données insuffisantes - " + String.join(", ", nextLine));
                        continue; // Ignore cette ligne
                    }
                    String isbn = nextLine[0];
                    String title = nextLine[1];
                    String author = nextLine[2];
                    String year_of_publication = nextLine[3];
                    String publisher = nextLine[4];
                    String imageUrlS = nextLine[5];
                    String imageUrlM = nextLine[6];
                    String imageUrlL = nextLine[7];

                    // Log pour vérifier les données
                    System.out.println("ISBN: " + isbn);
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("Year_Of_Publication: " + year_of_publication);
                    System.out.println("Publisher: " + publisher);
                    System.out.println("Image Small: " + imageUrlS);
                    System.out.println("Image Medium: " + imageUrlM);
                    System.out.println("Image Large: " + imageUrlL);

                    // Ici, insérer les données en base
                    Book book = new Book();
                    book.setIsbn(isbn);
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setYear_of_publication(year_of_publication);
                    book.setPublisher(publisher);
                    book.setAvailability(true);
                    book.setImageUrlS(imageUrlS);
                    book.setImageUrlM(imageUrlM);
                    book.setImageUrlL(imageUrlL);
                    book.setLibrary(libraryService.getDefaultLibrary());

                    bookService.createBook(book);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'importation des livres avec images : " + e.getMessage(), e);
        }
    }

}

