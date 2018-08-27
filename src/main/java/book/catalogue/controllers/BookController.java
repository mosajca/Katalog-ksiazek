package book.catalogue.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import book.catalogue.database.Book;
import book.catalogue.services.BookService;
import book.catalogue.utils.CSV;
import book.catalogue.utils.PDF;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(value = "/books/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getAllBooksPDF(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "inline; filename=\"books.pdf\"");
        return new PDF("Książki", bookService.getAllBooks()).generate();
    }

    @GetMapping(value = "/books/csv", produces = "text/csv; charset=utf-8")
    public byte[] getAllBooksCSV(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment; filename=\"books.csv\"");
        return CSV.toStringCSV(bookService.getAllBooksRecords()).getBytes(StandardCharsets.UTF_8);
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PostMapping("/books")
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    @PostMapping("/books/csv")
    public void importCSV(@RequestParam MultipartFile file) throws IOException {
        bookService.addBooks(CSV.fromStringCSV(new String(file.getBytes(), StandardCharsets.UTF_8)));
    }

    @PutMapping("/books/{id}")
    public void updateBook(@RequestBody Book book, @PathVariable Long id) {
        bookService.updateBook(book, id);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

}
