package esgi.services;

import esgi.Factory.BookFactory;
import esgi.config.DatabaseConnection;
import esgi.models.Book;
import esgi.models.Library;
import esgi.models.Role;
import esgi.models.User;
import esgi.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;
    private BookFactory bookFactory;

    public BookService(BookRepository bookRepository, BookFactory bookFactory) {
        this.bookRepository = bookRepository;
        this.bookFactory = bookFactory;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() ->new BookNotFoundException("Book not found with ID: " + id));
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.searchBooks(query);
    }


    public String updateBook(Book bookDetails) {


        Optional<Book> book_id = this.bookRepository.findById(bookDetails.getId());
        if (book_id.isPresent()) {
            Book book = book_id.get();

            if (bookDetails.getAuthor() != null){
                book.setAuthor(bookDetails.getAuthor());
            }
            if (bookDetails.getGenre() != null){
                book.setGenre(bookDetails.getGenre());
            }
            if (bookDetails.getTitle() != null){
                book.setTitle(bookDetails.getTitle());
            }
            if (bookDetails.getLibrary() != null){
                book.setLibrary(bookDetails.getLibrary());
            }
            this.bookRepository.save(book);
            return "Modification successfully completed";
        }

        return( "Book not found.");
    }

    public Book addBook(Book book) {

        return bookRepository.save(book);
    }

    public void deleteBook(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
    }


}
