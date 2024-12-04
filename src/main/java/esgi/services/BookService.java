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

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

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

}
