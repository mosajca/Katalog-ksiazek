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

import book.catalogue.database.Author;
import book.catalogue.database.Book;
import book.catalogue.services.AuthorService;
import book.catalogue.utils.CSV;
import book.catalogue.utils.PDF;

@RestController
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping(value = "/authors/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getAllAuthorsPDF(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "inline; filename=\"authors.pdf\"");
        return new PDF("Autorzy", authorService.getAllAuthors()).generate();
    }

    @GetMapping(value = "/authors/csv", produces = "text/csv; charset=utf-8")
    public byte[] getAllAuthorsCSV(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment; filename=\"authors.csv\"");
        return CSV.toStringCSV(authorService.getAllAuthorsRecords()).getBytes(StandardCharsets.UTF_8);
    }

    @GetMapping("/authors/{id}")
    public Author getAuthor(@PathVariable Long id) {
        return authorService.getAuthor(id);
    }

    @GetMapping("/authors/{id}/books")
    public List<Book> getAllBooks(@PathVariable Long id) {
        return authorService.getAllBooksOfAuthor(id);
    }

    @PostMapping("/authors")
    public void addAuthor(@RequestBody Author author) {
        authorService.addAuthor(author);
    }

    @PostMapping("/authors/csv")
    public void importCSV(@RequestParam MultipartFile file) throws IOException {
        authorService.addAuthors(CSV.fromStringCSV(new String(file.getBytes(), StandardCharsets.UTF_8)));
    }

    @PutMapping("/authors/{id}")
    public void updateAuthor(@RequestBody Author author, @PathVariable Long id) {
        authorService.updateAuthor(author, id);
    }

    @DeleteMapping("/authors/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

}
