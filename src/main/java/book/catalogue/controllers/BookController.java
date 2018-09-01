package book.catalogue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.database.Book;
import book.catalogue.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController extends GenericController<Book> {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        super(bookService, "books", "Książki");
        this.bookService = bookService;
    }

}
