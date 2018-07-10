package book.catalogue.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.database.Book;
import book.catalogue.services.BookService;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping("/books")
	public List<Book> getAllBooks() {
		return bookService.getAllBooks();
	}

	@GetMapping("/books/{id}")
	public Book getBook(@PathVariable Long id) {
		return bookService.getBook(id);
	}

	@PostMapping("/books")
	public void addBook(@RequestBody Book book) {
		bookService.addBook(book);
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
