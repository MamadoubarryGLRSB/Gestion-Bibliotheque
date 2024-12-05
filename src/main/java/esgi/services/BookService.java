package esgi.services;

import esgi.config.DatabaseConnection;
import esgi.models.Book;
import esgi.models.Library;
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
    private final LibraryService libraryService;

    public BookService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Récupère tous les livres de la base de données.
     *
     * @return Liste de tous les livres.
     */
    public List<Book> getAllBooks() {
        String query = "SELECT * FROM book";
        return executeQuery(query, null);
    }

    /**
     * Récupère un livre par son ID.
     *
     * @param id ID du livre.
     * @return Livre correspondant ou un Optional vide.
     */
    public Optional<Book> getBookById(Long id) {
        String query = "SELECT * FROM book WHERE id = ?";
        List<Book> books = executeQuery(query, ps -> ps.setLong(1, id));
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    /**
     * Crée un nouveau livre dans la base de données.
     *
     * @param book Livre à créer.
     * @return Livre créé avec l'ID généré.
     */
    public Book createBook(Book book) {
        if (book.getLibrary() == null) {
            Library defaultLibrary = libraryService.getDefaultLibrary();
            book.setLibrary(defaultLibrary);
        }

        String query = "INSERT INTO book (isbn, title, author, year_of_publication, publisher, genre, image_url_s, image_url_m, image_url_l, availability, id_library) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            prepareBookStatement(statement, book);
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                }
            }

            logger.info("Livre créé avec succès : {}", book);
        } catch (SQLException e) {
            logger.error("Erreur lors de la création du livre.", e);
            throw new BookServiceException("Erreur lors de la création du livre.", e);
        }

        return book;
    }

    /**
     * Met à jour un livre existant.
     *
     * @param id          ID du livre à mettre à jour.
     * @param bookDetails Détails du livre à mettre à jour.
     * @return Livre mis à jour.
     */
    public Optional<Book> updateBook(Long id, Book bookDetails) {
        String query = "UPDATE book SET isbn = ?, title = ?, author = ?, year_of_publication = ?, publisher = ?, genre = ?, image_url_s = ?, image_url_m = ?, image_url_l = ?, availability = ?, id_library = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            prepareBookStatement(statement, bookDetails);
            statement.setLong(12, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                return Optional.empty();
            }

            logger.info("Livre mis à jour avec succès : {}", bookDetails);
            return Optional.of(bookDetails);
        } catch (SQLException e) {
            logger.error("Erreur lors de la mise à jour du livre avec l'ID " + id, e);
            throw new BookServiceException("Erreur lors de la mise à jour du livre avec l'ID " + id, e);
        }
    }

    /**
     * Supprime un livre par son ID.
     *
     * @param id ID du livre à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean deleteBook(Long id) {
        String query = "DELETE FROM book WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                logger.info("Livre supprimé avec succès : ID {}", id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression du livre avec l'ID " + id, e);
            throw new BookServiceException("Erreur lors de la suppression du livre avec l'ID " + id, e);
        }
    }

    /**
     * Prépare un `PreparedStatement` avec les informations d'un livre.
     *
     * @param statement Requête préparée.
     * @param book      Livre à insérer ou mettre à jour.
     * @throws SQLException En cas d'erreur SQL.
     */
    private void prepareBookStatement(PreparedStatement statement, Book book) throws SQLException {
        statement.setString(1, book.getIsbn());
        statement.setString(2, book.getTitle());
        statement.setString(3, book.getAuthor());
        statement.setString(4, book.getYearOfPublication());
        statement.setString(5, book.getPublisher());
        statement.setString(6, book.getGenre());
        statement.setString(7, book.getImageUrlS());
        statement.setString(8, book.getImageUrlM());
        statement.setString(9, book.getImageUrlL());
        statement.setBoolean(10, book.isAvailability());
        statement.setLong(11, book.getLibrary().getId());
    }

    /**
     * Récupère une liste paginée de livres.
     *
     * @param page  Page à récupérer.
     * @param limit Nombre de livres par page.
     * @return Liste paginée de livres.
     */
    public List<Book> getBooksPaginated(int page, int limit) {
        String query = "SELECT * FROM book LIMIT ? OFFSET ?";
        return executeQuery(query, ps -> {
            ps.setInt(1, limit);
            ps.setInt(2, page * limit);
        });
    }

    /**
     * Compte le nombre total de livres dans la base de données.
     *
     * @return Nombre total de livres.
     */
    public long countBooks() {
        String query = "SELECT COUNT(*) FROM book";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des livres.", e);
            throw new BookServiceException("Erreur lors du comptage des livres.", e);
        }
        return 0;
    }

    /**
     * Exécute une requête SQL et mappe les résultats en objets `Book`.
     *
     * @param query    Requête SQL à exécuter.
     * @param consumer Consommateur pour paramétrer la requête.
     * @return Liste des objets `Book` résultants.
     */
    private List<Book> executeQuery(String query, PreparedStatementConsumer consumer) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (consumer != null) {
                consumer.accept(statement);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de l'exécution de la requête.", e);
            throw new BookServiceException("Erreur lors de l'exécution de la requête.", e);
        }

        return books;
    }

    /**
     * Mappe un ResultSet en un objet Book.
     *
     * @param resultSet Résultat de la requête SQL.
     * @return Objet `Book` mappé.
     * @throws SQLException si une erreur survient lors du mappage.
     */
    private Book mapResultSetToBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        book.setYearOfPublication(resultSet.getString("year_of_publication"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setGenre(resultSet.getString("genre"));
        book.setImageUrlS(resultSet.getString("image_url_s"));
        book.setImageUrlM(resultSet.getString("image_url_m"));
        book.setImageUrlL(resultSet.getString("image_url_l"));
        book.setAvailability(resultSet.getBoolean("availability"));
        return book;
    }

    /**
     * Interface fonctionnelle pour consommer un PreparedStatement.
     */
    @FunctionalInterface
    private interface PreparedStatementConsumer {
        void accept(PreparedStatement ps) throws SQLException;
    }
}
