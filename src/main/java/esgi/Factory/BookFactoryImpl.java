package esgi.Factory;

import esgi.models.Book;
import esgi.models.Library;
import org.springframework.stereotype.Component;

@Component
public class BookFactoryImpl implements BookFactory{
    @Override
    public Book createBook(String title, String author, String genre, String image, boolean availability, Library library) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setImage(image);
        book.setAvailability(availability);
        book.setLibrary(library);
        return book;
    }
}
