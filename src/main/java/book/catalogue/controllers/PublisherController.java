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
import book.catalogue.database.Publisher;
import book.catalogue.services.PublisherService;

@RestController
public class PublisherController {

	@Autowired
	private PublisherService publisherService;

	@GetMapping("/publishers")
	public List<Publisher> getAllPublishers() {
		return publisherService.getAllPublishers();
	}

	@GetMapping("/publishers/{id}")
	public Publisher getPublisher(@PathVariable Long id) {
		return publisherService.getPublisher(id);
	}

	@GetMapping("/publishers/{id}/books")
	public List<Book> getAllBooks(@PathVariable Long id) {
		return publisherService.getPublisher(id).getBooks();
	}

	@PostMapping("/publishers")
	public void addPublisher(@RequestBody Publisher publisher) {
		publisherService.addPublisher(publisher);
	}

	@PutMapping("/publishers/{id}")
	public void updatePublisher(@RequestBody Publisher publisher) {
		publisherService.updatePublisher(publisher);
	}

	@DeleteMapping("/publishers/{id}")
	public void deletePublisher(@PathVariable Long id) {
		publisherService.deletePublisher(id);
	}

}
