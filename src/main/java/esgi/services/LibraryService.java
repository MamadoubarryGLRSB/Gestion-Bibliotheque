package esgi.services;

import esgi.models.Book;
import esgi.models.Library;
import esgi.models.User;
import esgi.repositories.BookRepository;
import esgi.repositories.LibraryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private LibraryRepository libraryRepository;
    private BookRepository bookRepository;

    public LibraryService(LibraryRepository libraryRepository, BookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    public List<Library> getAllLibraries() {
        return libraryRepository.findAll();
    }
    public Library getLibraryById(Long id) {
        return libraryRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Library not found"));
    }

    public Library createLibrary(Library library) {
        return libraryRepository.save(library);
    }

    public String updateLibrary(Long id, Library libraryDetails) {
        Library library = getLibraryById(id);
        if (library != null){
            library.setName(libraryDetails.getName());
            libraryRepository.save(library);
            return "Update sucess";
        }else{
            return "Error Update";
        }


    }

    public String deleteLibrary(Long id) {

        Optional<Library> libraryDelete = this.libraryRepository.findById(id);
        List<Book> listBook= this.bookRepository.findAll();

        if (libraryDelete.isPresent()){
           Library library =  libraryDelete.get();

            for (Book book : listBook) {
                if (book.getLibrary().getId().equals(id) ){
                    return "A book belongs to this category";
                }
            }
            this.libraryRepository.delete(library);
            return "Library delete with sucess ";
        }
        else {
            return "Library does not exist";
        }





    }
}
