package esgi.repository;

import esgi.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    public List<Book> findByAuthor(String author);
    public List<Book> findByTitle(String title);
    public List<Book> findByGenre(String genre);
}
