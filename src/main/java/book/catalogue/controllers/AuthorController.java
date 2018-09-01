package book.catalogue.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.database.Author;
import book.catalogue.database.Book;
import book.catalogue.services.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController extends GenericController<Author> {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        super(authorService, "authors", "Autorzy");
        this.authorService = authorService;
    }

    @GetMapping("{id}/books")
    public List<Book> getAllBooks(@PathVariable Long id) {
        return authorService.getAllBooksOfAuthor(id);
    }

}
