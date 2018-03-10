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

import book.catalogue.database.Author;
import book.catalogue.services.AuthorService;

@RestController
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@GetMapping("/authors")
	public List<Author> getAllAuthors() {
		return authorService.getAllAuthors();
	}

	@GetMapping("/authors/{id}")
	public Author getAuthor(@PathVariable Long id) {
		return authorService.getAuthor(id);
	}

	@PostMapping("/authors")
	public void addAuthor(@RequestBody Author author) {
		authorService.addAuthor(author);
	}

	@PutMapping("/authors/{id}")
	public void updateAuthor(@RequestBody Author author) {
		authorService.updateAuthor(author);
	}

	@DeleteMapping("/authors/{id}")
	public void deleteAuthor(@PathVariable Long id) {
		authorService.deleteAuthor(id);
	}

}
