package esgi.controllers;

import esgi.models.Library;
import esgi.services.BookService;
import esgi.services.LibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/libraries")
public class LibraryController {
    private LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public List<Library> getAllLibraries() {
        return libraryService.getAllLibraries();
    }

    @GetMapping("/{id}")
    public Library getLibraryById(@PathVariable Long id) {
        return libraryService.getLibraryById(id);
    }

    @PostMapping(path= "/create", consumes = APPLICATION_JSON_VALUE)
    public Library createLibrary(@RequestBody Library library) {
        return libraryService.createLibrary(library);
    }

    @PutMapping("/update/{id}")
    public String updateLibrary(@PathVariable Long id, @RequestBody Library library) {
        return this.libraryService.updateLibrary(id, library);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteLibrary(@PathVariable Long id) {

        return this.libraryService.deleteLibrary(id);
    }
}
