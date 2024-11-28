package esgi.services;

import esgi.config.DatabaseConnection;
import esgi.models.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setGenre(resultSet.getString("genre"));
                book.setImage(resultSet.getString("image"));
                book.setAvailability(resultSet.getBoolean("availability"));
                books.add(book);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des livres.", e); // Log de l'erreur
            throw new RuntimeException("Erreur lors de la récupération des livres.", e);
        }

        return books;
    }

    public Optional<Book> getBookById(Long id) {
        String query = "SELECT * FROM book WHERE id = ?";
        Book book = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setGenre(resultSet.getString("genre"));
                book.setImage(resultSet.getString("image"));
                book.setAvailability(resultSet.getBoolean("availability"));
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération du livre avec l'ID " + id, e); // Log de l'erreur
            throw new RuntimeException("Erreur lors de la récupération du livre avec l'ID " + id, e);
        }

        return Optional.ofNullable(book);
    }

    public Book createBook(Book book) {
        String query = "INSERT INTO book (title, author, genre, image, availability, id_library) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setString(4, book.getImage());
            statement.setBoolean(5, book.isAvailability());
            statement.setLong(6, book.getLibrary().getId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                }
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la création du livre.", e); // Log de l'erreur
            throw new RuntimeException("Erreur lors de la création du livre.", e);
        }

        return book;
    }

    public Book updateBook(Long id, Book bookDetails) {
        String query = "UPDATE book SET title = ?, author = ?, genre = ?, image = ?, availability = ?, id_library = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, bookDetails.getTitle());
            statement.setString(2, bookDetails.getAuthor());
            statement.setString(3, bookDetails.getGenre());
            statement.setString(4, bookDetails.getImage());
            statement.setBoolean(5, bookDetails.isAvailability());
            statement.setLong(6, bookDetails.getLibrary().getId());
            statement.setLong(7, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("Aucun livre trouvé avec l'ID " + id);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la mise à jour du livre avec l'ID " + id, e); // Log de l'erreur
            throw new RuntimeException("Erreur lors de la mise à jour du livre avec l'ID " + id, e);
        }

        return bookDetails;
    }

    public void deleteBook(Long id) {
        String query = "DELETE FROM book WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted == 0) {
                throw new RuntimeException("Aucun livre trouvé avec l'ID " + id);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression du livre avec l'ID " + id, e); // Log de l'erreur
            throw new RuntimeException("Erreur lors de la suppression du livre avec l'ID " + id, e);
        }
    }
}
