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
        return bookRepository.findAll();
    }

    // Ajouter un livre
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    // Supprimer un livre
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Mettre à jour un livre
    public Book updateBook(Long id, Book updatedBook) {
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
    }

    // Filtrer les livres en fonction des critères
    public List<Book> filterBooks(String title, String author, String genre) {
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
    }

    // Récupérer un livre par son ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
}
