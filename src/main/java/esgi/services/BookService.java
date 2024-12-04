package esgi.services;

import esgi.models.Book;
import esgi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Récupérer tous les livres
    public List<Book> getAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des livres.", e);
        }
    }

    // Ajouter un livre
    public Book addBook(Book book) {
        try {
            return bookRepository.save(book);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout du livre.", e);
        }
    }

    public void deleteBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);


        if (!bookOptional.isPresent()) {
            throw new RuntimeException("Livre avec l'ID " + id + " non trouvé.");
        }

        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du livre avec l'ID " + id, e);
        }
    }


    // Mettre à jour un livre
    public Book updateBook(Long id, Book updatedBook) {
        try {
            Optional<Book> optionalBook = bookRepository.findById(id);
            if (optionalBook.isPresent()) {
                Book existingBook = optionalBook.get();
                existingBook.setTitle(updatedBook.getTitle());
                existingBook.setAuthor(updatedBook.getAuthor());
                existingBook.setGenre(updatedBook.getGenre());
                existingBook.setImage(updatedBook.getImage());
                existingBook.setAvailability(updatedBook.isAvailability());
                existingBook.setLibrary(updatedBook.getLibrary());
                return bookRepository.save(existingBook);
            } else {
                throw new RuntimeException("Book not found with id " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du livre avec l'ID " + id, e);
        }
    }

    // Filtrer les livres en fonction des critères
    public List<Book> filterBooks(String title, String author, String genre) {
        try {
            if (title != null && !title.isEmpty()) {
                return bookRepository.findByTitle(title);
            }
            if (author != null && !author.isEmpty()) {
                return bookRepository.findByAuthor(author);
            }
            if (genre != null && !genre.isEmpty()) {
                return bookRepository.findByGenre(genre);
            }
            return bookRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du filtrage des livres.", e);
        }
    }

    // Récupérer un livre par son ID
    public Optional<Book> getBookById(Long id) {
        try {
            return bookRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du livre avec l'ID " + id, e);
        }
    }
}
