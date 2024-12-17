package esgi.Factory;

import esgi.models.Book;
import esgi.models.Library;

public interface BookFactory {
    Book createBook(String title, String author, String genre, String image, boolean availability, Library library);

}