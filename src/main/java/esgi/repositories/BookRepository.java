package esgi.repositories;

import esgi.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Recherche par titre
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Recherche par auteur
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Recherche par genre
    List<Book> findByGenreContainingIgnoreCase(String genre);

    // Recherche avancée : titre, auteur ou genre
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchBooks(@Param("query") String query);

}
